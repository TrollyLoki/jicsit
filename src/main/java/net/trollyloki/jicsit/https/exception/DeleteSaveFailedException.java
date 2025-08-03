package net.trollyloki.jicsit.https.exception;

/**
 * Failed to delete the save file.
 */
public class DeleteSaveFailedException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected DeleteSaveFailedException(ErrorResponse response) {
        super(response);
    }

}
