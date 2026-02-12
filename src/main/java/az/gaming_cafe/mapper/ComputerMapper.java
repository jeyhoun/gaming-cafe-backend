package az.gaming_cafe.mapper;

import az.gaming_cafe.model.dto.request.ComputerRequestDto;
import az.gaming_cafe.model.dto.response.ComputerResponseDto;
import az.gaming_cafe.model.entity.ComputerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ComputerMapper {

    List<ComputerResponseDto> toDtoList(List<ComputerEntity> computers);
    ComputerResponseDto toDto(ComputerEntity computer);
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    @Mapping(target = "createdBy",ignore = true)
    @Mapping(target = "updatedBy",ignore = true)
    @Mapping(target = "id",ignore = true)
    ComputerEntity toEntity(ComputerRequestDto computerRequestDto);
}
