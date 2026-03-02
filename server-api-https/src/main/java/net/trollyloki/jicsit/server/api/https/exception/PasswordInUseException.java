package net.trollyloki.jicsit.server.api.https.exception;

import org.jspecify.annotations.NullMarked;

/**
 * The provided password is already used as the other password.
 */
@NullMarked
public class PasswordInUseException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    PasswordInUseException(ErrorResponse response) {
        super(response);
    }

}
