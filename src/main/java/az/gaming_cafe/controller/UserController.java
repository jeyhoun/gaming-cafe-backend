package az.gaming_cafe.controller;

import az.gaming_cafe.model.dto.common.ApiResult;
import az.gaming_cafe.model.dto.response.UserResponseDto;
import az.gaming_cafe.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ApiResult<UserResponseDto> getUser() {
        return ApiResult.ok(userService.getCurrentUser());
    }
}
