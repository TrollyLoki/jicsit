package net.trollyloki.jicsit.server.api.https.exception;

import net.trollyloki.jicsit.server.api.https.HttpsApiClient;

/**
 * Passwordless login is not currently possible.
 *
 * @see HttpsApiClient#removeClientPassword()
 */
public class PasswordlessLoginNotPossibleException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected PasswordlessLoginNotPossibleException(ErrorResponse response) {
        super(response);
    }

}
