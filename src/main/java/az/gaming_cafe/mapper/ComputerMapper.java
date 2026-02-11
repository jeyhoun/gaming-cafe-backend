package az.gaming_cafe.mapper;

import az.gaming_cafe.model.dto.response.ComputerResponseDto;
import az.gaming_cafe.model.entity.ComputerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ComputerMapper {

    List<ComputerResponseDto> toDtoList(List<ComputerEntity> computers);
}
