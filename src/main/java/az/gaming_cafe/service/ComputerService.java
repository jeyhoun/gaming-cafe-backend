package az.gaming_cafe.service;

import az.gaming_cafe.model.dto.request.ComputerRequestDto;
import az.gaming_cafe.model.dto.response.ComputerResponseDto;

import java.util.List;

public interface ComputerService {

    List<ComputerResponseDto> getAllComputers();
    ComputerResponseDto getComputerById(Long id);
    ComputerResponseDto createComputer(ComputerRequestDto computerRequestDto);
    ComputerResponseDto updateComputer(Long id,ComputerRequestDto computerRequestDto);
}
