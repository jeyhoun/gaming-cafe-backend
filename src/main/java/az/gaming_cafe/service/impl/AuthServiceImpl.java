package az.gaming_cafe.service.impl;

import az.gaming_cafe.TrackUserAction;
import az.gaming_cafe.component.dto.RequestContext;
import az.gaming_cafe.config.JwtProperties;
import az.gaming_cafe.config.SecurityProperties;
import az.gaming_cafe.exception.ApplicationException;
import az.gaming_cafe.exception.data.ErrorCode;
import az.gaming_cafe.model.dto.request.RefreshTokenRequestDto;
import az.gaming_cafe.model.dto.request.SignInRequestDto;
import az.gaming_cafe.model.dto.request.SignUpRequestDto;
import az.gaming_cafe.model.dto.response.RefreshTokenResponseDto;
import az.gaming_cafe.model.dto.response.SignInResponseDto;
import az.gaming_cafe.model.dto.response.SignUpResponseDto;
import az.gaming_cafe.model.entity.RefreshTokenEntity;
import az.gaming_cafe.model.entity.RevokedTokenEntity;
import az.gaming_cafe.model.entity.RoleEntity;
import az.gaming_cafe.model.entity.UserEntity;
import az.gaming_cafe.repository.RefreshTokenRepository;
import az.gaming_cafe.repository.RevokedTokenRepository;
import az.gaming_cafe.repository.RoleRepository;
import az.gaming_cafe.repository.UserRepository;
import az.gaming_cafe.security.rbac.JwtUtils;
import az.gaming_cafe.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RevokedTokenRepository revokedTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtil;
    private final JwtProperties jwtProperties;
    private final SecurityProperties securityProperties;


    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           RevokedTokenRepository revokedTokenRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtil,
                           JwtProperties jwtProperties,
                           SecurityProperties securityProperties) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.revokedTokenRepository = revokedTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.jwtProperties = jwtProperties;
        this.securityProperties = securityProperties;
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
        return SignInResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .accessToken(token)
                .refreshToken(refreshTokenData.getRefreshToken())
                .expiresIn(jwtProperties.getAccessTokenExpiration() / 1000)
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
        return SignUpResponseDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .accessToken(token)
                .refreshToken(refreshTokenData.getRefreshToken())
                .expiresIn(jwtProperties.getAccessTokenExpiration() / 1000)
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
        if (currentUseCount >= securityProperties.getMaxRefreshTokenUse()) {
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
        return RefreshTokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenData.getRefreshToken())
                .expiresIn(jwtProperties.getAccessTokenExpiration() / 1000)
                .build();
    }

    @Override
    @Transactional
    public void logout() {
        log.info("ActionLog.logout.start");

        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("ActionLog.logout.userNotFound username: {}", username);
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

                log.info("ActionLog.logout.accessTokenRevoked jti: {}", jti);
                //CHECKSTYLE:OFF
            } catch (Exception e) {
                log.warn("ActionLog.logout.couldNotRevokeAccessToken error: {}", e.getMessage());
            } //CHECKSTYLE:ON
        }

        log.info("ActionLog.logout.end");
    }

    private void saveRefreshTokenJti(String jti, RequestContext ctx, UserEntity user) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setJti(jti);
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setExpiryDate(
                LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenExpiration() / 1000)
        );
        refreshTokenEntity.setIpAddress(ctx.getIpAddress());
        refreshTokenEntity.setUserAgent(ctx.getUserAgent());
        refreshTokenRepository.save(refreshTokenEntity);
        log.info("ActionLog.saveRefreshTokenJti.end");
    }

    public boolean isExpired(LocalDateTime expiryDate) {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
