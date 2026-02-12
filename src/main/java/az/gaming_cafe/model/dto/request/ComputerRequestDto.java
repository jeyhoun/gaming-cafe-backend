package az.gaming_cafe.model.dto.request;

import az.gaming_cafe.model.enums.ComputerStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Map;

public class ComputerRequestDto {

    @NotBlank(message = "Computer name cannot be empty")
    @Size(max = 50, message = "Computer name cannot exceed 50 characters")
    private String name;
    private ComputerStatus status;
    @Pattern(
            regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$",
            message = "Invalid IPv4 address format"
    )
    private String ipAddress;
    @Size(min = 1, max = 10, message = "Specs map must contain between {min} and {max} entries")
    private Map<String,String> specs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ComputerStatus getStatus() {
        return status;
    }

    public void setStatus(ComputerStatus status) {
        this.status = status;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Map<String, String> getSpecs() {
        return specs;
    }

    public void setSpecs(Map<String, String> specs) {
        this.specs = specs;
    }
}
