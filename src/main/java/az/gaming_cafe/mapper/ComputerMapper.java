package az.gaming_cafe.mapper;

import az.gaming_cafe.model.dto.response.ComputerResponseDto;
import az.gaming_cafe.model.entity.ComputerEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComputerMapper {


    List<ComputerResponseDto> toDtoList(List<ComputerEntity> computers);
}
