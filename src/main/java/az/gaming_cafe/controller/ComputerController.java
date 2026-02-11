package az.gaming_cafe.controller;

import az.gaming_cafe.model.dto.common.ApiResult;
import az.gaming_cafe.model.dto.response.ComputerResponseDto;
import az.gaming_cafe.service.ComputerService;
import az.gaming_cafe.service.impl.ComputerServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/computers")
public class ComputerController {

    private final ComputerService computerService;

    public ComputerController(ComputerServiceImpl computerService) {
        this.computerService = computerService;
    }

    @GetMapping
    public ApiResult<List<ComputerResponseDto>> getAllComputers() {
        return ApiResult.ok(computerService.getAllComputers());
    }
}
