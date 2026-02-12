package az.gaming_cafe.service;

import az.gaming_cafe.component.dto.RequestContext;
import az.gaming_cafe.model.dto.request.ForgotPasswordRequestDto;
import az.gaming_cafe.model.dto.request.RefreshTokenRequestDto;
import az.gaming_cafe.model.dto.request.ResetPasswordRequestDto;
import az.gaming_cafe.model.dto.request.SignInRequestDto;
import az.gaming_cafe.model.dto.request.SignUpRequestDto;
import az.gaming_cafe.model.dto.response.RefreshTokenResponseDto;
import az.gaming_cafe.model.dto.response.SignInResponseDto;
import az.gaming_cafe.model.dto.response.SignUpResponseDto;
import az.gaming_cafe.model.dto.response.TokenVerifyResponseDto;

public interface AuthService {

    SignInResponseDto signIn(SignInRequestDto request, RequestContext context);
    SignUpResponseDto signUp(SignUpRequestDto request, RequestContext context);
    RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto request, RequestContext context);
    void signOut();
    void forgotPassword(ForgotPasswordRequestDto request);
    TokenVerifyResponseDto verifyReset(String token);
    void resetPassword(ResetPasswordRequestDto request);
}
