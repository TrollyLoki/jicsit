package net.trollyloki.jicsit.server.https.exception;

import org.jspecify.annotations.NullMarked;

/**
 * The specified authentication token is invalid.
 */
@NullMarked
public class TokenValidationException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    TokenValidationException(ErrorResponse response) {
        super(response);
    }

}
