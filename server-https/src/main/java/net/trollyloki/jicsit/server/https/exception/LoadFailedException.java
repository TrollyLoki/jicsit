package net.trollyloki.jicsit.server.https.exception;

import org.jspecify.annotations.NullMarked;

/**
 * Failed to load the save file.
 */
@NullMarked
public class LoadFailedException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    LoadFailedException(ErrorResponse response) {
        super(response);
    }

}
