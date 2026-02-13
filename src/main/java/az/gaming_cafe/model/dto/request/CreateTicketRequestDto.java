package az.gaming_cafe.model.dto.request;

import az.gaming_cafe.model.enums.TicketCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class CreateTicketRequestDto {

    @NotNull(message = "Subject is required")
    @NotBlank(message = "Subject is required")
    private String subject;

    @NotNull(message = "Message is required")
    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Category is required")
    @NotBlank(message = "Category is required")
    private TicketCategory category;

    public CreateTicketRequestDto() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TicketCategory getCategory() {
        return category;
    }

    public void setCategory(TicketCategory category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateTicketRequestDto that = (CreateTicketRequestDto) o;
        return Objects.equals(subject, that.subject) &&
                Objects.equals(message, that.message) &&
                category == that.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, message, category);
    }

    @Override
    public String toString() {
        return "CreateTicketRequest{" +
                "subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", category=" + category +
                '}';
    }
}
