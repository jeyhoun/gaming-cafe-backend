package az.gaming_cafe.service;

import az.gaming_cafe.model.dto.request.SignInRequest;
import az.gaming_cafe.model.dto.request.SignUpRequestDto;
import az.gaming_cafe.model.dto.response.SignInResponseDto;
import az.gaming_cafe.model.dto.response.SignUpResponseDto;

public interface AuthService {
    SignInResponseDto signIn(SignInRequest request);

    SignUpResponseDto signUp(SignUpRequestDto request);
}
