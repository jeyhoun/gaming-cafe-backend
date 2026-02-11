package az.gaming_cafe.mapper;

import az.gaming_cafe.model.dto.response.ComputerResponseDto;
import az.gaming_cafe.model.entity.ComputerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ComputerMapper {

    List<ComputerResponseDto> toDtoList(List<ComputerEntity> computers);
    ComputerResponseDto toDto(ComputerEntity computer);
}
