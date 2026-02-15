package az.gaming_cafe.service.impl;

import az.gaming_cafe.exception.ApplicationException;
import az.gaming_cafe.exception.data.ErrorCode;
import az.gaming_cafe.mapper.UserMapper;
import az.gaming_cafe.model.dto.response.UserResponseDto;
import az.gaming_cafe.model.entity.BaseAuditEntity;
import az.gaming_cafe.model.entity.UserEntity;
import az.gaming_cafe.repository.UserHistoryRepository;
import az.gaming_cafe.repository.UserRepository;
import az.gaming_cafe.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;

    public UserServiceImpl(UserRepository userRepository, UserHistoryRepository userHistoryRepository) {
        this.userRepository = userRepository;
        this.userHistoryRepository = userHistoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUser() {
        log.info("ActionLog.getCurrentUser.start");
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        LocalDateTime lastLogin = userHistoryRepository
                .findFirstByUserIdAndActionAndStatusOrderByCreatedAtDesc(user.getId(), "SIGN_IN", "SUCCESS")
                .map(BaseAuditEntity::getCreatedAt)
                .orElse(null);

        log.info("ActionLog.getCurrentUser.end");

        return INSTANCE.toDto(user, lastLogin);
    }
}
