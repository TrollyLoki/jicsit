package net.trollyloki.jicsit.https.exception;

/**
 * The specified save file does not exist.
 */
public class FileNotFoundException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected FileNotFoundException(ErrorResponse response) {
        super(response);
    }

}
