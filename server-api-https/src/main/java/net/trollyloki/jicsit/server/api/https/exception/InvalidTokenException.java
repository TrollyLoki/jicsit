package net.trollyloki.jicsit.server.api.https.exception;

import net.trollyloki.jicsit.server.api.https.HttpsApi;
import net.trollyloki.jicsit.server.api.https.PrivilegeLevel;

/**
 * The client's authentication token is invalid.
 *
 * @see HttpsApi#passwordlessLogin(PrivilegeLevel)
 * @see HttpsApi#passwordLogin(PrivilegeLevel, String)
 */
public class InvalidTokenException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected InvalidTokenException(ErrorResponse response) {
        super(response);
    }

}
