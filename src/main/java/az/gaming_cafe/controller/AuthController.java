package az.gaming_cafe.controller;

import az.gaming_cafe.component.dto.RequestContext;
import az.gaming_cafe.model.dto.common.ApiResult;
import az.gaming_cafe.model.dto.request.ForgotPasswordRequestDto;
import az.gaming_cafe.model.dto.request.RefreshTokenRequestDto;
import az.gaming_cafe.model.dto.request.ResetPasswordRequestDto;
import az.gaming_cafe.model.dto.request.SignInRequestDto;
import az.gaming_cafe.model.dto.request.SignUpRequestDto;
import az.gaming_cafe.model.dto.response.RefreshTokenResponseDto;
import az.gaming_cafe.model.dto.response.SignInResponseDto;
import az.gaming_cafe.model.dto.response.SignUpResponseDto;
import az.gaming_cafe.model.dto.response.TokenVerifyResponseDto;
import az.gaming_cafe.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public ApiResult<SignInResponseDto> signIn(@RequestBody SignInRequestDto request, RequestContext context) {
        return ApiResult.ok(authService.signIn(request, context));
    }

    @PostMapping("/sign-up")
    public ApiResult<SignUpResponseDto> signUp(@RequestBody @Valid SignUpRequestDto request, RequestContext context) {
        return ApiResult.ok(authService.signUp(request, context));
    }

    @PostMapping("/refresh-token")
    public ApiResult<RefreshTokenResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto request, RequestContext context) {
        return ApiResult.ok(authService.refreshToken(request, context));
    }

    @PostMapping("/logout")
    public ApiResult<Void> signOut() {
        authService.signOut();
        return ApiResult.ok();
    }

    @PostMapping("/forgot-password")
    public ApiResult<Void> forgotPassword(@RequestBody ForgotPasswordRequestDto request) {
        authService.forgotPassword(request);
        return ApiResult.ok();
    }

    @GetMapping("/reset-password/verify")
    public ApiResult<TokenVerifyResponseDto> verifyReset(@RequestParam(name = "token") String token) {
        return ApiResult.ok(authService.verifyReset(token));
    }

    @PostMapping("/reset-password")
    public ApiResult<Void> resetPassword(@RequestBody ResetPasswordRequestDto request) {
        authService.resetPassword(request);
        return ApiResult.ok();
    }
}
