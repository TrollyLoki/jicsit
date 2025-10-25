package net.trollyloki.jicsit.server.api.https.exception;

import net.trollyloki.jicsit.server.api.https.HttpsApiClient;
import net.trollyloki.jicsit.server.api.https.PrivilegeLevel;

/**
 * The client is missing required privileges to access the requested function.
 *
 * @see HttpsApiClient#passwordlessLogin(PrivilegeLevel)
 * @see HttpsApiClient#passwordLogin(PrivilegeLevel, String)
 */
public class InsufficientPrivilegesException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected InsufficientPrivilegesException(ErrorResponse response) {
        super(response);
    }

}
