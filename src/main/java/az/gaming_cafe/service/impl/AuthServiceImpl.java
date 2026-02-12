package az.gaming_cafe.service.impl;

import az.gaming_cafe.TrackUserAction;
import az.gaming_cafe.component.dto.RequestContext;
import az.gaming_cafe.exception.ApplicationException;
import az.gaming_cafe.exception.data.ErrorCode;
import az.gaming_cafe.model.dto.request.ForgotPasswordRequestDto;
import az.gaming_cafe.model.dto.request.RefreshTokenRequestDto;
import az.gaming_cafe.model.dto.request.ResetPasswordRequestDto;
import az.gaming_cafe.model.dto.request.SignInRequestDto;
import az.gaming_cafe.model.dto.request.SignUpRequestDto;
import az.gaming_cafe.model.dto.response.RefreshTokenResponseDto;
import az.gaming_cafe.model.dto.response.SignInResponseDto;
import az.gaming_cafe.model.dto.response.SignUpResponseDto;
import az.gaming_cafe.model.dto.response.TokenVerifyResponseDto;
import az.gaming_cafe.model.entity.PasswordResetTokenEntity;
import az.gaming_cafe.model.entity.RefreshTokenEntity;
import az.gaming_cafe.model.entity.RevokedTokenEntity;
import az.gaming_cafe.model.entity.RoleEntity;
import az.gaming_cafe.model.entity.UserEntity;
import az.gaming_cafe.repository.PasswordResetTokenRepository;
import az.gaming_cafe.repository.RefreshTokenRepository;
import az.gaming_cafe.repository.RevokedTokenRepository;
import az.gaming_cafe.repository.RoleRepository;
import az.gaming_cafe.repository.UserRepository;
import az.gaming_cafe.security.rbac.JwtUtils;
import az.gaming_cafe.service.AuthService;
import az.gaming_cafe.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RevokedTokenRepository revokedTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtil;
    private final EmailService emailService;

    @Value("${jwt.access-token.expiration:900000}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration:604800000}")
    private long refreshTokenExpiration;

    @Value("${security.max-refresh-token-use:1}")
    private int maxRefreshTokenUse;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           RevokedTokenRepository revokedTokenRepository,
                           PasswordResetTokenRepository passwordResetTokenRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtil,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.revokedTokenRepository = revokedTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    @TrackUserAction
    public SignInResponseDto signIn(SignInRequestDto request, RequestContext context) {
        log.info("ActionLog.signIn.start");

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("ActionLog.signIn.userNotFound email: {}", request.getEmail());
                    return new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("ActionLog.signIn.invalidPassword email: {}", request.getEmail());
            throw new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
        }

        if (!user.isActive()) {
            throw new ApplicationException(ErrorCode.USER_INACTIVE);
        }

        refreshTokenRepository.revokeAllUserTokens(user.getId());

        String token = jwtUtil.generateAccessToken(user);
        JwtUtils.TokenWithJti refreshTokenData = jwtUtil.generateRefreshToken(user.getUsername());

        saveRefreshTokenJti(refreshTokenData.getJti(), context, user);

        log.info("ActionLog.signIn.end");
        return SignInResponseDto.builder()//fixme move to mapper
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .accessToken(token)
                .refreshToken(refreshTokenData.getRefreshToken())
                .expiresIn(accessTokenExpiration / 1000)
                .build();
    }

    @Override
    @Transactional
    @TrackUserAction
    public SignUpResponseDto signUp(SignUpRequestDto request, RequestContext context) {
        log.info("ActionLog.signUp.start");
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApplicationException(ErrorCode.USER_ALREADY_EXISTS);
        }

        UserEntity newUser = new UserEntity();
        newUser.setEmail(request.getEmail());
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        RoleEntity userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_ROLE_NOT_FOUND));
        newUser.getRoles().add(userRole);

        UserEntity savedUser = userRepository.save(newUser);

        String token = jwtUtil.generateAccessToken(savedUser);
        JwtUtils.TokenWithJti refreshTokenData = jwtUtil.generateRefreshToken(savedUser.getUsername());

        saveRefreshTokenJti(refreshTokenData.getJti(), context, savedUser);

        log.info("ActionLog.signUp.end");
        return SignUpResponseDto.builder()//fixme move to mapper
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .accessToken(token)
                .refreshToken(refreshTokenData.getRefreshToken())
                .expiresIn(accessTokenExpiration / 1000)
                .build();
    }

    @Override
    @Transactional
    @TrackUserAction
    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto request, RequestContext context) {
        log.info("ActionLog.refreshToken.start");
        String refreshToken = request.getRefreshToken();

        String jti;
        try {
            jti = jwtUtil.extractJti(refreshToken);
            //CHECKSTYLE:OFF
        } catch (Exception e) {
            log.warn("ActionLog.refreshToken.invalidToken");
            throw new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN);
        } //CHECKSTYLE:ON

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByJtiWithLock(jti)
                .orElseThrow(() -> {
                    log.warn("ActionLog.refreshToken.jtiNotFound");
                    return new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
                });

        if (refreshTokenEntity.isRevoked()) {
            log.warn("ActionLog.refreshToken.revoked - REPLAY ATTACK DETECTED! jti: {}", jti);
            refreshTokenRepository.revokeAllUserTokens(refreshTokenEntity.getUser().getId());
            throw new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
        }

        if (isExpired(refreshTokenEntity.getExpiryDate())) {
            refreshTokenRepository.delete(refreshTokenEntity);
            throw new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
        }

        UserEntity user = refreshTokenEntity.getUser();
        if (!jwtUtil.isTokenValid(refreshToken, user.getUsername())) {
            throw new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
        }

        int currentUseCount = refreshTokenEntity.getUseCount();
        if (currentUseCount >= maxRefreshTokenUse) {
            refreshTokenRepository.revokeAllUserTokens(user.getId());
            throw new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
        }

        refreshTokenEntity.setUseCount(currentUseCount + 1);
        refreshTokenEntity.setLastUsedAt(LocalDateTime.now());

        String newAccessToken = jwtUtil.generateAccessToken(user);
        JwtUtils.TokenWithJti newRefreshTokenData = jwtUtil.generateRefreshToken(user.getUsername());

        refreshTokenEntity.setRevoked(true);
        refreshTokenRepository.save(refreshTokenEntity);

        saveRefreshTokenJti(newRefreshTokenData.getJti(), context, user);

        log.info("ActionLog.refreshToken.end");
        return RefreshTokenResponseDto.builder()//fixme move to mapper
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenData.getRefreshToken())
                .expiresIn(accessTokenExpiration / 1000)
                .build();
    }

    @Override
    @Transactional
    public void signOut() {
        log.info("ActionLog.signOut.start");

        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("ActionLog.signOut.userNotFound username: {}", username);
                    return new ApplicationException(ErrorCode.USER_NOT_FOUND);
                });

        refreshTokenRepository.revokeAllUserTokens(user.getId());

        jakarta.servlet.http.HttpServletRequest request =
                ((org.springframework.web.context.request.ServletRequestAttributes)
                        org.springframework.web.context.request.RequestContextHolder.getRequestAttributes())
                        .getRequest();
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String accessToken = authHeader.substring(7);
                String jti = jwtUtil.extractJti(accessToken);
                LocalDateTime expiration = jwtUtil.extractExpiration(accessToken)
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                RevokedTokenEntity revokedToken = new RevokedTokenEntity();
                revokedToken.setJti(jti);
                revokedToken.setExpiryDate(expiration);
                revokedTokenRepository.save(revokedToken);

                log.info("ActionLog.signOut.accessTokenRevoked jti: {}", jti);
                //CHECKSTYLE:OFF
            } catch (Exception e) {
                log.warn("ActionLog.signOut.couldNotRevokeAccessToken error: {}", e.getMessage());
            } //CHECKSTYLE:ON
        }

        log.info("ActionLog.signOut.end");
    }

    @Override
    public void forgotPassword(ForgotPasswordRequestDto request) {
        log.info("ActionLog.forgotPassword.start");
        Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            log.info("ActionLog.forgotPassword.end email not found");
            return;
        }

        UserEntity user = userOpt.get();
        passwordResetTokenRepository.findByUser(user)
                .ifPresent(passwordResetTokenRepository::delete);

        String token = UUID.randomUUID().toString();

        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        resetToken.setUsed(false);

        passwordResetTokenRepository.save(resetToken);

        String resetLink = frontendUrl + "/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
        log.info("ActionLog.forgotPassword.end");
    }

    @Override
    public TokenVerifyResponseDto verifyReset(String token) {
        log.info("ActionLog.verifyReset.start");
        Optional<PasswordResetTokenEntity> resetTokenOpt =
                passwordResetTokenRepository.findByTokenWithUser(token);

        if (resetTokenOpt.isEmpty()) {
            return TokenVerifyResponseDto.builder().isValid(false).build();
        }
        PasswordResetTokenEntity resetToken = resetTokenOpt.get();

        log.info("ActionLog.verifyReset.end");
        boolean isOk = !resetToken.isUsed() && resetToken.getExpiryDate().isAfter(LocalDateTime.now());

        return TokenVerifyResponseDto.builder().isValid(isOk).build();//fixme move to mapper
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequestDto request) {
        log.info("ActionLog.resetPassword.start");
        PasswordResetTokenEntity resetToken = passwordResetTokenRepository.findByTokenWithUser(request.getToken())
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_TOKEN));

        if (resetToken.isUsed()) {
            throw new ApplicationException(ErrorCode.TOKEN_ALREADY_USED);
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ApplicationException(ErrorCode.TOKEN_EXPIRED);
        }

        UserEntity user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
        log.info("ActionLog.resetPassword.end");
    }

    private void saveRefreshTokenJti(String jti, RequestContext ctx, UserEntity user) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setJti(jti);
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setExpiryDate(
                LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000)
        );
        refreshTokenEntity.setIpAddress(ctx.getIpAddress());
        refreshTokenEntity.setUserAgent(ctx.getUserAgent());
        refreshTokenRepository.save(refreshTokenEntity);
        log.info("ActionLog.saveRefreshTokenJti.end");
    }

    private boolean isExpired(LocalDateTime expiryDate) {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
