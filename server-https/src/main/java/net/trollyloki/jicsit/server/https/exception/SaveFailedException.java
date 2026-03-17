package net.trollyloki.jicsit.server.https.exception;

import org.jspecify.annotations.NullMarked;

/**
 * Failed to create the save file.
 */
@NullMarked
public class SaveFailedException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    SaveFailedException(ErrorResponse response) {
        super(response);
    }

}
