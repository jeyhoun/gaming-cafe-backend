package az.gaming_cafe.model.dto.response;

public class TokenVerifyResponseDto {

    private boolean isValid;

    public TokenVerifyResponseDto(boolean isOk) {
        this.isValid = isOk;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {

        private boolean isValid;

        public Builder isValid(boolean isOk) {
            this.isValid = isOk;
            return this;
        }

        public TokenVerifyResponseDto build() {
            return new TokenVerifyResponseDto(isValid);
        }
    }
}
