package net.trollyloki.jicsit.server.api.https.exception;

/**
 * Failed to load the save file.
 */
public class LoadFailedException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected LoadFailedException(ErrorResponse response) {
        super(response);
    }

}
