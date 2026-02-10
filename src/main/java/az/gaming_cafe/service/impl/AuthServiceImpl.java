package az.gaming_cafe.service.impl;

import az.gaming_cafe.exception.InvalidCredentialsException;
import az.gaming_cafe.exception.InvalidRefreshTokenException;
import az.gaming_cafe.exception.UserAlreadyExistsException;
import az.gaming_cafe.exception.UserInactiveException;
import az.gaming_cafe.model.dto.request.RefreshTokenRequestDto;
import az.gaming_cafe.model.dto.request.SignInRequestDto;
import az.gaming_cafe.model.dto.request.SignUpRequestDto;
import az.gaming_cafe.model.dto.response.RefreshTokenResponseDto;
import az.gaming_cafe.model.dto.response.SignInResponseDto;
import az.gaming_cafe.model.dto.response.SignUpResponseDto;
import az.gaming_cafe.model.entity.RefreshTokenEntity;
import az.gaming_cafe.model.entity.RoleEntity;
import az.gaming_cafe.model.entity.UserEntity;
import az.gaming_cafe.repository.RefreshTokenRepository;
import az.gaming_cafe.repository.RoleRepository;
import az.gaming_cafe.repository.UserRepository;
import az.gaming_cafe.security.rbac.JwtUtils;
import az.gaming_cafe.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtil;

    @Value("${jwt.access-token.expiration:900000}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration:604800000}")
    private long refreshTokenExpiration;

    @Value("${security.max-refresh-token-use:1}")
    private int maxRefreshTokenUse;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public SignInResponseDto signIn(SignInRequestDto request) {
        log.info("ActionLog.signIn.start");

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("ActionLog.signIn.userNotFound email: {}", request.getEmail());
                    return new InvalidCredentialsException();
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("ActionLog.signIn.invalidPassword email: {}", request.getEmail());
            throw new InvalidCredentialsException();
        }

        if (!user.isActive()) {
            throw new UserInactiveException("User account is inactive");
        }

        refreshTokenRepository.revokeAllUserTokens(user.getId());

        String token = jwtUtil.generateAccessToken(user);
        JwtUtils.TokenWithJti refreshTokenData = jwtUtil.generateRefreshToken(user.getUsername());

        saveRefreshTokenJti(refreshTokenData.getJti(), null, null, user);

        log.info("ActionLog.signIn.end");
        return SignInResponseDto.builder()
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
    public SignUpResponseDto signUp(SignUpRequestDto request) {
        log.info("ActionLog.signUp.start");
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        UserEntity newUser = new UserEntity();
        newUser.setEmail(request.getEmail());
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        RoleEntity userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("User Role not found"));
        newUser.getRoles().add(userRole);

        UserEntity savedUser = userRepository.save(newUser);

        String token = jwtUtil.generateAccessToken(savedUser);
        JwtUtils.TokenWithJti refreshTokenData = jwtUtil.generateRefreshToken(savedUser.getUsername());

        saveRefreshTokenJti(refreshTokenData.getJti(), null, null, savedUser);

        log.info("ActionLog.signUp.end");
        return SignUpResponseDto.builder()
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
    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto request) {
        log.info("ActionLog.refreshToken.start");
        String refreshToken = request.getRefreshToken();

        String jti;
        try {
            jti = jwtUtil.extractJti(refreshToken);
            //CHECKSTYLE:OFF
        } catch (Exception e) {
            log.warn("ActionLog.refreshToken.invalidToken");
            throw new InvalidRefreshTokenException("Invalid refresh token");
        } //CHECKSTYLE:ON

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByJti(jti)
                .orElseThrow(() -> {
                    log.warn("ActionLog.refreshToken.jtiNotFound");
                    return new InvalidRefreshTokenException("Invalid refresh token");
                });

        if (refreshTokenEntity.isRevoked()) {
            log.warn("ActionLog.refreshToken.revoked - REPLAY ATTACK DETECTED! jti: {}", jti);
            refreshTokenRepository.revokeAllUserTokens(refreshTokenEntity.getUser().getId());
            throw new InvalidRefreshTokenException("Token reuse detected - all sessions terminated");
        }

        if (isExpired(refreshTokenEntity.getExpiryDate())) {
            log.warn("ActionLog.refreshToken.expired jti: {}", jti);
            refreshTokenRepository.delete(refreshTokenEntity);
            throw new InvalidRefreshTokenException("Refresh token has expired");
        }

        UserEntity user = refreshTokenEntity.getUser();
        if (!jwtUtil.isTokenValid(refreshToken, user.getUsername())) {
            log.warn("ActionLog.refreshToken.invalidJwtSignature");
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        refreshTokenEntity.setUseCount(refreshTokenEntity.getUseCount() + 1);
        refreshTokenEntity.setLastUsedAt(LocalDateTime.now());

        if (refreshTokenEntity.getUseCount() > maxRefreshTokenUse) {
            log.warn("ActionLog.refreshToken.tooManyUses jti: {}, count: {}",
                    jti, refreshTokenEntity.getUseCount());
            refreshTokenRepository.revokeAllUserTokens(user.getId());
            throw new InvalidRefreshTokenException("Suspicious activity detected");
        }

        refreshTokenRepository.save(refreshTokenEntity);

        String newAccessToken = jwtUtil.generateAccessToken(user);
        JwtUtils.TokenWithJti newRefreshTokenData = jwtUtil.generateRefreshToken(user.getUsername());

        refreshTokenEntity.setRevoked(true);
        refreshTokenRepository.save(refreshTokenEntity);

        saveRefreshTokenJti(newRefreshTokenData.getJti(), null, null, user);

        log.info("ActionLog.refreshToken.end");
        return RefreshTokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenData.getRefreshToken())
                .expiresIn(accessTokenExpiration / 1000)
                .build();
    }

    @Override
    public void logout(RefreshTokenRequestDto request) {
        log.info("ActionLog.logout.start");

        try {
            String jti = jwtUtil.extractJti(request.getRefreshToken());

            refreshTokenRepository.findByJti(jti).ifPresent(token -> {
                token.setRevoked(true);
                refreshTokenRepository.save(token);
                log.info("ActionLog.logout.success userId: {}", token.getUser().getId());
            });
            //CHECKSTYLE:OFF
        } catch (Exception e) {
            log.warn("ActionLog.logout.invalidToken");
        } //CHECKSTYLE:ON

        log.info("ActionLog.logout.end");
    }

    private void saveRefreshTokenJti(String jti, String ipAddress, String userAgent, UserEntity user) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setJti(jti);
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setExpiryDate(
                LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000)
        );
        refreshTokenEntity.setIpAddress(ipAddress);
        refreshTokenEntity.setUserAgent(userAgent);
        refreshTokenRepository.save(refreshTokenEntity);
        log.info("ActionLog.saveRefreshTokenJti.end");
    }

    public boolean isExpired(LocalDateTime expiryDate) {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
