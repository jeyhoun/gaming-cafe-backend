package az.gaming_cafe.service.impl;

import az.gaming_cafe.mapper.ComputerMapper;
import az.gaming_cafe.model.dto.response.ComputerResponseDto;
import az.gaming_cafe.model.entity.ComputerEntity;
import az.gaming_cafe.repository.ComputerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComputerService {

    private final ComputerRepository computerRepository;
    private final ComputerMapper computerMapper;

    public ComputerService(ComputerRepository computerRepository,
                           ComputerMapper computerMapper) {

        this.computerRepository = computerRepository;
        this.computerMapper = computerMapper;
    }

    public List<ComputerResponseDto> getAllComputers() {

        List<ComputerEntity> computers = computerRepository.findAll();
        return computerMapper.toDtoList(computers);
    }


}
