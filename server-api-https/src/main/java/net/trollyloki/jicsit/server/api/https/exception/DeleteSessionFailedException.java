package net.trollyloki.jicsit.server.api.https.exception;

/**
 * Failed to delete the session.
 */
public class DeleteSessionFailedException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected DeleteSessionFailedException(ErrorResponse response) {
        super(response);
    }

}
