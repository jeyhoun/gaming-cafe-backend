package az.gaming_cafe.model.dto.request;

import az.gaming_cafe.model.enums.TicketCategory;

import java.util.Objects;

public class CreateTicketRequestDto {

    private String subject;
    private String message;
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
