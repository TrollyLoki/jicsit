package net.trollyloki.jicsit.server.api.https.exception;

import org.jspecify.annotations.NullMarked;

/**
 * The save file has invalid encoding, a malformed header, or corrupted contents.
 */
@NullMarked
public class InvalidSaveException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    InvalidSaveException(ErrorResponse response) {
        super(response);
    }

}
