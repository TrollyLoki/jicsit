package net.trollyloki.jicsit.server.api.https;

/**
 * An exception that occurred while making an HTTPS API request.
 */
public class RequestException extends RuntimeException {

    /**
     * Creates a new request exception with the specified detail message.
     *
     * @param message detail message
     */
    RequestException(String message) {
        super(message);
    }

    /**
     * Creates a new request exception with the specified detail message and cause.
     *
     * @param message detail message
     * @param cause   underlying cause
     */
    RequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
