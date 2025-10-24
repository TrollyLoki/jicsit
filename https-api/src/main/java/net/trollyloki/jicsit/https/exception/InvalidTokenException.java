package net.trollyloki.jicsit.https.exception;

import net.trollyloki.jicsit.https.HttpsApiClient;
import net.trollyloki.jicsit.https.PrivilegeLevel;

/**
 * The client's authentication token is invalid.
 *
 * @see HttpsApiClient#passwordlessLogin(PrivilegeLevel)
 * @see HttpsApiClient#passwordLogin(PrivilegeLevel, String)
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
