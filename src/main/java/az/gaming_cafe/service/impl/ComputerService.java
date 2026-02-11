package az.gaming_cafe.service.impl;

import az.gaming_cafe.model.dto.response.ComputerResponseDto;
import az.gaming_cafe.model.entity.ComputerEntity;
import az.gaming_cafe.repository.ComputerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComputerService {

    private final ComputerRepository computerRepository;

    public ComputerService(ComputerRepository computerRepository) {
        this.computerRepository = computerRepository;
    }

    public List<ComputerResponseDto> getAllComputers() {
        return computerRepository.findAll()
                .stream()
                .map(computer -> ComputerResponseDto.builder()
                        .id(computer.getId())
                        .name(computer.getName())
                        .ipAddress(computer.getIpAddress())
                        .status(computer.getStatus())
                        .specs(computer.getSpecs())
                        .createdAt(computer.getCreatedAt())
                        .build())
                .toList();
    }


}
