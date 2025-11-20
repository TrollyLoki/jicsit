package net.trollyloki.jicsit.server.api.https.exception;

import net.trollyloki.jicsit.server.api.https.HttpsApi;

/**
 * The server has already been claimed.
 *
 * @see HttpsApi#renameServer(String)
 * @see HttpsApi#setAdminPassword(String)
 */
public class ServerAlreadyClaimedException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected ServerAlreadyClaimedException(ErrorResponse response) {
        super(response);
    }

}
