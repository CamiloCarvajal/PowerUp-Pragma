package co.com.camilo.model.exceptions;

public class BusinessException extends RuntimeException {
    private final String errorCode;

    protected BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
