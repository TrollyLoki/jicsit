package net.trollyloki.jicsit.server.api.https.exception;

import org.jspecify.annotations.NullMarked;

/**
 * The specified save file does not exist.
 */
@NullMarked
public class FileNotFoundException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    FileNotFoundException(ErrorResponse response) {
        super(response);
    }

}
