package az.gaming_cafe.service.impl;

import az.gaming_cafe.exception.InvalidCredentialsException;
import az.gaming_cafe.exception.UserAlreadyExistsException;
import az.gaming_cafe.exception.UserInactiveException;
import az.gaming_cafe.model.dto.request.SignInRequestDto;
import az.gaming_cafe.model.dto.request.SignUpRequestDto;
import az.gaming_cafe.model.dto.response.SignInResponseDto;
import az.gaming_cafe.model.dto.response.SignUpResponseDto;
import az.gaming_cafe.model.entity.RoleEntity;
import az.gaming_cafe.model.entity.UserEntity;
import az.gaming_cafe.repository.RoleRepository;
import az.gaming_cafe.repository.UserRepository;
import az.gaming_cafe.security.rbac.JwtUtils;
import az.gaming_cafe.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public SignInResponseDto signIn(SignInRequestDto request) {
        log.info("ActionLog.signIn.start request email: {}", request.getEmail());

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

        Map<String, Object> claims = new HashMap<>();

        Set<String> roles = user.getRoles().stream().map(RoleEntity::getName)
                .collect(Collectors.toSet());

        claims.put("roles", roles);
        claims.put("userId", user.getId());

        String token = jwtUtil.generateToken(user.getUsername(), claims);

        log.info("ActionLog.signIn.end email: {}", user.getEmail());

        return SignInResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .token(token)
                .build();
    }

    @Override
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto request) {
        log.info("ActionLog.signUp.start request email: {}", request.getEmail());
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
        String token = jwtUtil.generateToken(newUser.getUsername(), Map.of("role", "USER"));

        log.info("ActionLog.signUp.end request email: {}", savedUser.getEmail());
        return SignUpResponseDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .token(token)
                .build();
    }
}
