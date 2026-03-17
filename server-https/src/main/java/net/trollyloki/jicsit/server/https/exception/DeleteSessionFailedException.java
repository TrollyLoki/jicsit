package net.trollyloki.jicsit.server.https.exception;

import org.jspecify.annotations.NullMarked;

/**
 * Failed to delete the session.
 */
@NullMarked
public class DeleteSessionFailedException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    DeleteSessionFailedException(ErrorResponse response) {
        super(response);
    }

}
