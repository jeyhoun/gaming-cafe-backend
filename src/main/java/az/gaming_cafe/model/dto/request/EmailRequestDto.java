package az.gaming_cafe.model.dto.request;

public class EmailRequestDto {

    private String from;
    private String to;
    private String replyTo;
    private String subject;
    private String message;

    public EmailRequestDto() {
    }

    private EmailRequestDto(Builder builder) {
        this.from = builder.from;
        this.to = builder.to;
        this.replyTo = builder.replyTo;
        this.subject = builder.subject;
        this.message = builder.message;
    }

    // Getter və Setter-lər
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getReplyTo() { return replyTo; }
    public void setReplyTo(String replyTo) { this.replyTo = replyTo; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {
        private String from;
        private String to;
        private String replyTo;
        private String subject;
        private String message;

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder to(String to) {
            this.to = to;
            return this;
        }

        public Builder replyTo(String replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public EmailRequestDto build() {
            return new EmailRequestDto(this);
        }
    }
}
