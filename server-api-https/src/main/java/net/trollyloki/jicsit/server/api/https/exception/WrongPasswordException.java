package net.trollyloki.jicsit.server.api.https.exception;

import org.jspecify.annotations.NullMarked;

/**
 * The provided password did not match the password set on the server.
 */
@NullMarked
public class WrongPasswordException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    WrongPasswordException(ErrorResponse response) {
        super(response);
    }

}
