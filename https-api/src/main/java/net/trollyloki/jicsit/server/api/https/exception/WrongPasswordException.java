package net.trollyloki.jicsit.server.api.https.exception;

/**
 * The provided password did not match the password set on the server.
 */
public class WrongPasswordException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected WrongPasswordException(ErrorResponse response) {
        super(response);
    }

}
