package net.trollyloki.jicsit.server.api.https.exception;

/**
 * The provided password was empty, but it cannot be removed.
 */
public class CannotRemovePasswordException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected CannotRemovePasswordException(ErrorResponse response) {
        super(response);
    }

}
