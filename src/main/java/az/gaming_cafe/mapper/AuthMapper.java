package az.gaming_cafe.mapper;

import az.gaming_cafe.model.dto.response.RefreshTokenResponseDto;
import az.gaming_cafe.model.dto.response.SignInResponseDto;
import az.gaming_cafe.model.dto.response.SignUpResponseDto;
import az.gaming_cafe.model.dto.response.TokenVerifyResponseDto;
import az.gaming_cafe.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AuthMapper {

    @Mapping(target = "accessToken",source = "accessToken")
    @Mapping(target = "refreshToken",source = "refreshToken")
    @Mapping(target = "expiresIn",source = "expiresIn")
    SignInResponseDto toSignInResponse(UserEntity user,
                                       String accessToken,
                                       String refreshToken,
                                       Long expiresIn);
    @Mapping(target = "accessToken",source = "accessToken")
    @Mapping(target = "refreshToken",source = "refreshToken")
    @Mapping(target = "expiresIn",source = "expiresIn")
    SignUpResponseDto toSignUpResponse(UserEntity user,
                                       String accessToken,
                                       String refreshToken,
                                       Long expiresIn);
    @Mapping(target = "accessToken",source = "accessToken")
    @Mapping(target = "refreshToken",source = "refreshToken")
    @Mapping(target = "expiresIn",source = "expiresIn")
    RefreshTokenResponseDto toRefreshTokenResponse( String accessToken,
                                                    String refreshToken,
                                                    Long expiresIn);
    @Mapping(target = "isValid",source = "isValid")
    TokenVerifyResponseDto toTokenVerifyResponse(Boolean isValid);
}
