package net.trollyloki.jicsit.https.exception;

import net.trollyloki.jicsit.https.HttpsApiClient;

/**
 * The server has already been claimed.
 *
 * @see HttpsApiClient#renameServer(String)
 * @see HttpsApiClient#setAdminPassword(String)
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
