package net.trollyloki.jicsit.server.api.https.exception;

/**
 * The provided password is already used as the other password.
 */
public class PasswordInUseException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected PasswordInUseException(ErrorResponse response) {
        super(response);
    }

}
