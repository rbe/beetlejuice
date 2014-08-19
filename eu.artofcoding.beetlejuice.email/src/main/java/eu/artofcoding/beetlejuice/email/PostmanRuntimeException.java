package eu.artofcoding.beetlejuice.email;

public class PostmanRuntimeException extends RuntimeException {

    public PostmanRuntimeException() {
    }

    public PostmanRuntimeException(String message) {
        super(message);
    }

    public PostmanRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostmanRuntimeException(Throwable cause) {
        super(cause);
    }

    public PostmanRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
