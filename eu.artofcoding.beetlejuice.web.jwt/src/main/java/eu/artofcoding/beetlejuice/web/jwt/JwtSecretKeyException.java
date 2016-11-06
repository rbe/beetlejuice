package eu.artofcoding.beetlejuice.web.jwt;

public class JwtSecretKeyException extends RuntimeException {

    public JwtSecretKeyException(final Throwable cause) {
        super(cause);
    }

    public JwtSecretKeyException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
