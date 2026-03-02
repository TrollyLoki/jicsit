package net.trollyloki.jicsit.server.api.https.exception;

import org.jspecify.annotations.NullMarked;

/**
 * The save file is too old (or too new) to be loaded.
 */
@NullMarked
public class UnsupportedSaveException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    UnsupportedSaveException(ErrorResponse response) {
        super(response);
    }

}
