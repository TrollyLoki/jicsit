package net.trollyloki.jicsit.server.https.exception;

import org.jspecify.annotations.NullMarked;

/**
 * Failed to enumerate session saves.
 */
@NullMarked
public class EnumerateSessionsException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    EnumerateSessionsException(ErrorResponse response) {
        super(response);
    }

}
