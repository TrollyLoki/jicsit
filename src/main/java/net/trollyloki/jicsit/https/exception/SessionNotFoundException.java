package net.trollyloki.jicsit.https.exception;

/**
 * The specified session does not exist.
 */
public class SessionNotFoundException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected SessionNotFoundException(ErrorResponse response) {
        super(response);
    }

}
