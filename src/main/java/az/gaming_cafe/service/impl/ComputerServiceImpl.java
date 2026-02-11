package az.gaming_cafe.service.impl;

import az.gaming_cafe.exception.ApplicationException;
import az.gaming_cafe.exception.data.ErrorCode;
import az.gaming_cafe.mapper.ComputerMapper;
import az.gaming_cafe.model.dto.response.ComputerResponseDto;
import az.gaming_cafe.model.entity.ComputerEntity;
import az.gaming_cafe.repository.ComputerRepository;
import az.gaming_cafe.service.ComputerService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ComputerServiceImpl implements ComputerService {

    private final ComputerMapper computerMapper = Mappers.getMapper(ComputerMapper.class);

    private final ComputerRepository computerRepository;

    public ComputerServiceImpl(ComputerRepository computerRepository) {
        this.computerRepository = computerRepository;
    }

    @Override
    public List<ComputerResponseDto> getAllComputers() {
        log.info("ActionLog.getAllComputers.start");
        List<ComputerEntity> computers = computerRepository.findAll();
        log.info("ActionLog.getAllComputers.end");
        return computerMapper.toDtoList(computers);
    }

    @Override
    public ComputerResponseDto getComputerById(Long id) {
        log.info("ActionLog.getComputerById.start");
        ComputerEntity computer = computerRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.COMPUTER_NOT_FOUND));
        log.info("ActionLog.getComputerById.end");
        return computerMapper.toDto(computer);


    }


}
