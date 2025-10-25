package net.trollyloki.jicsit.server.api.https.exception;

/**
 * The save file is too old (or too new) to be loaded.
 */
public class UnsupportedSaveException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected UnsupportedSaveException(ErrorResponse response) {
        super(response);
    }

}
