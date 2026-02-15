package az.gaming_cafe.mapper;

import az.gaming_cafe.model.dto.response.UserResponseDto;
import az.gaming_cafe.model.entity.RoleEntity;
import az.gaming_cafe.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "lastLoginAt", source = "lastLogin")
    UserResponseDto toDto(UserEntity user, LocalDateTime lastLogin);

    default List<String> map(Set<RoleEntity> roles) {
        if (roles == null) {
            return List.of();
        }

        return roles.stream()
                .map(RoleEntity::getName)
                .toList();
    }
}
