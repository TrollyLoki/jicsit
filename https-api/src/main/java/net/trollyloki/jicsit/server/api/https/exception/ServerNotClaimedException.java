package net.trollyloki.jicsit.server.api.https.exception;

import net.trollyloki.jicsit.server.api.https.HttpsApi;

/**
 * The server has not been claimed yet.
 *
 * @see HttpsApi#claimServer(String, String)
 */
public class ServerNotClaimedException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected ServerNotClaimedException(ErrorResponse response) {
        super(response);
    }

}
