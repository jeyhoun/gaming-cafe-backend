package az.gaming_cafe.service.impl;

import az.gaming_cafe.mapper.ComputerMapper;
import az.gaming_cafe.model.dto.response.ComputerResponseDto;
import az.gaming_cafe.model.entity.ComputerEntity;
import az.gaming_cafe.repository.ComputerRepository;
import az.gaming_cafe.service.ComputerService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComputerServiceImpl implements ComputerService {

    private final ComputerRepository computerRepository;
    private final ComputerMapper computerMapper = Mappers.getMapper(ComputerMapper.class);

    public ComputerServiceImpl(ComputerRepository computerRepository) {

        this.computerRepository = computerRepository;
    }

    public List<ComputerResponseDto> getAllComputers() {

        List<ComputerEntity> computers = computerRepository.findAll();
        return computerMapper.toDtoList(computers);
    }


}
