package az.gaming_cafe.controller;

import az.gaming_cafe.model.dto.common.ApiResult;
import az.gaming_cafe.model.dto.request.SignInRequest;
import az.gaming_cafe.model.dto.request.SignUpRequestDto;
import az.gaming_cafe.model.dto.response.SignInResponseDto;
import az.gaming_cafe.model.dto.response.SignUpResponseDto;
import az.gaming_cafe.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public ApiResult<SignInResponseDto> signIn(@RequestBody SignInRequest request) {
        return ApiResult.ok(authService.signIn(request));
    }

    @PostMapping("/sign-up")
    public ApiResult<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto request) {
        return ApiResult.ok(authService.signUp(request));
    }

//    @PreAuthorize("@permissionEvaluator.canCancelReservation(authentication, #reservation)")
//    @DeleteMapping("/reservations/{id}")
//    public void cancel(@PathVariable Reservation reservation) {
//        // reservation cancel logic
//    }
}
