package net.trollyloki.jicsit.server.https.exception;

import net.trollyloki.jicsit.server.https.HttpsApi;
import org.jspecify.annotations.NullMarked;

/**
 * Passwordless login is not currently possible.
 *
 * @see HttpsApi#removeClientPassword()
 */
@NullMarked
public class PasswordlessLoginNotPossibleException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    PasswordlessLoginNotPossibleException(ErrorResponse response) {
        super(response);
    }

}
