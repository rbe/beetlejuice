package eu.artofcoding.beetlejuice.email;

public class PostmanException extends Exception {

    public PostmanException() {
    }

    public PostmanException(String message) {
        super(message);
    }

    public PostmanException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostmanException(Throwable cause) {
        super(cause);
    }

    public PostmanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
