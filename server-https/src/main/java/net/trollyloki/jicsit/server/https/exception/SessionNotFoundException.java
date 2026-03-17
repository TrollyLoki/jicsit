package net.trollyloki.jicsit.server.https.exception;

import org.jspecify.annotations.NullMarked;

/**
 * The specified session does not exist.
 */
@NullMarked
public class SessionNotFoundException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    SessionNotFoundException(ErrorResponse response) {
        super(response);
    }

}
