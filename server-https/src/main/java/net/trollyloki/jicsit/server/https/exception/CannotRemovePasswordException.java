package net.trollyloki.jicsit.server.https.exception;

import org.jspecify.annotations.NullMarked;

/**
 * The provided password was empty, but it cannot be removed.
 */
@NullMarked
public class CannotRemovePasswordException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    CannotRemovePasswordException(ErrorResponse response) {
        super(response);
    }

}
