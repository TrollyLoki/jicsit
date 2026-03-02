package net.trollyloki.jicsit.server.api.https.exception;

import net.trollyloki.jicsit.server.api.https.HttpsApi;
import net.trollyloki.jicsit.server.api.https.PrivilegeLevel;
import org.jspecify.annotations.NullMarked;

/**
 * The client's authentication token is invalid.
 *
 * @see HttpsApi#passwordlessLogin(PrivilegeLevel)
 * @see HttpsApi#passwordLogin(PrivilegeLevel, String)
 */
@NullMarked
public class InvalidTokenException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    InvalidTokenException(ErrorResponse response) {
        super(response);
    }

}
