package net.trollyloki.jicsit.server.api.https.exception;

/**
 * Failed to create the save file.
 */
public class SaveFailedException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected SaveFailedException(ErrorResponse response) {
        super(response);
    }

}
