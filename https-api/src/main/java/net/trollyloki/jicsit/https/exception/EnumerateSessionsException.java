package net.trollyloki.jicsit.https.exception;

/**
 * Failed to enumerate session saves.
 */
public class EnumerateSessionsException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected EnumerateSessionsException(ErrorResponse response) {
        super(response);
    }

}
