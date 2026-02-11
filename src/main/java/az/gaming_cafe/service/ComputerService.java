package az.gaming_cafe.service;

import az.gaming_cafe.model.dto.response.ComputerResponseDto;

import java.util.List;

public interface ComputerService {

    List<ComputerResponseDto> getAllComputers();
}
