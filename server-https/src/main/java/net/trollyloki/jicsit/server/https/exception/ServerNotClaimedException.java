package net.trollyloki.jicsit.server.https.exception;

import net.trollyloki.jicsit.server.https.HttpsApi;
import org.jspecify.annotations.NullMarked;

/**
 * The server has not been claimed yet.
 *
 * @see HttpsApi#claimServer(String, String)
 */
@NullMarked
public class ServerNotClaimedException extends ApiException {

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    ServerNotClaimedException(ErrorResponse response) {
        super(response);
    }

}
