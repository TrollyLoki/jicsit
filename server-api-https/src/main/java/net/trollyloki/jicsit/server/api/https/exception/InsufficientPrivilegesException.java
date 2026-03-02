package net.trollyloki.jicsit.server.api.https.exception;

import net.trollyloki.jicsit.server.api.https.HttpsApi;
import net.trollyloki.jicsit.server.api.https.PrivilegeLevel;
import org.jspecify.annotations.NullMarked;

/**
 * The client is missing required privileges to access the requested function.
 *
 * @see HttpsApi#passwordlessLogin(PrivilegeLevel)
 * @see HttpsApi#passwordLogin(PrivilegeLevel, String)
 */
@NullMarked
public class InsufficientPrivilegesException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    InsufficientPrivilegesException(ErrorResponse response) {
        super(response);
    }

}
