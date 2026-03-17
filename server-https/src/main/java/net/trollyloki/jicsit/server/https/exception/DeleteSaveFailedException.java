package net.trollyloki.jicsit.server.https.exception;

import org.jspecify.annotations.NullMarked;

/**
 * Failed to delete the save file.
 */
@NullMarked
public class DeleteSaveFailedException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    DeleteSaveFailedException(ErrorResponse response) {
        super(response);
    }

}
