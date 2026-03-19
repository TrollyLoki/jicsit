package net.trollyloki.jicsit.server;

import net.trollyloki.jicsit.server.https.HttpsApi;
import net.trollyloki.jicsit.server.https.trustmanager.FingerprintBasedTrustManager;
import net.trollyloki.jicsit.server.https.trustmanager.InsecureTrustManager;
import net.trollyloki.jicsit.server.query.QueryApi;
import org.jspecify.annotations.Nullable;

import javax.net.ssl.TrustManager;
import java.net.SocketException;
import java.time.Duration;

/**
 * An interface for the vanilla HTTPS and Lightweight Query API functions.
 */
public interface ServerApi extends QueryApi, HttpsApi {

    /**
     * Creates a new {@link ServerApi} instance for the vanilla HTTPS and Lightweight Query APIs on a specific server.
     * An API authentication token can be obtained by running
     * the {@code server.GenerateAPIToken} command in the dedicated server's console.
     * <p>
     * Note that the returned {@link ServerApi} will
     * <strong>refuse to connect to servers using self-signed certificates</strong>
     * unless you provide a custom trust manager.
     * <p>
     * The following custom trust manager implementations may be helpful
     * when attempting to connect to servers that are using self-signed certificates:
     * <ul>
     *     <li>
     *         {@link FingerprintBasedTrustManager}
     *         can be created with the fingerprint displayed by the game
     *         and will verify that the server's certificate matches it when connecting.
     *     </li>
     *     <li>
     *         {@link InsecureTrustManager}
     *         simply accepts any certificate presented by the server.
     *         <strong>This is not secure</strong> but could be useful when connecting to unknown servers.
     *     </li>
     * </ul>
     *
     * @param host         server host name
     * @param port         server port
     * @param timeout      duration to wait for responses, or {@code null} to wait indefinitely
     * @param trustManager custom trust manager to use, or {@code null} to use the default trust manager
     * @param token        authentication token, or {@code null} to make unauthenticated requests
     * @return new {@link ServerApi} instance
     * @throws IllegalArgumentException if {@code timeout} is non-positive or {@code host}/{@code port} is invalid
     * @throws SocketException          if the query socket could not be opened
     */
    static ServerApi of(String host, int port, @Nullable Duration timeout, @Nullable TrustManager trustManager, @Nullable String token) throws SocketException {
        return new ServerApiImpl(QueryApi.of(host, port, timeout), HttpsApi.of(host, port, timeout, trustManager, token));
    }

}
