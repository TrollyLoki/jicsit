package net.trollyloki.jicsit.server.api.https.exception;

/**
 * The save file has invalid encoding, a malformed header, or corrupted contents.
 */
public class InvalidSaveException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected InvalidSaveException(ErrorResponse response) {
        super(response);
    }

}
