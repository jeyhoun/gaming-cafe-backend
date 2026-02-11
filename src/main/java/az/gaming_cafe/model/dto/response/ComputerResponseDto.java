package az.gaming_cafe.model.dto.response;

import az.gaming_cafe.model.enums.ComputerStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;


@Setter
@Getter
@Builder
public class ComputerResponseDto {

    private Long id;

    private String name;

    private ComputerStatus status;

    private String ipAddress;

    private Map<String, String> specs;

    private LocalDateTime createdAt;

}
