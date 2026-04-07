package net.trollyloki.jicsit.server;

import net.trollyloki.jicsit.server.https.AdvancedGameSettings;
import net.trollyloki.jicsit.server.https.HttpsApi;
import net.trollyloki.jicsit.server.https.PrivilegeLevel;
import net.trollyloki.jicsit.server.https.RequestException;
import net.trollyloki.jicsit.server.https.ServerGameState;
import net.trollyloki.jicsit.server.https.ServerOptions;
import net.trollyloki.jicsit.server.https.ServerSessions;
import net.trollyloki.jicsit.server.https.exception.ApiException;
import net.trollyloki.jicsit.server.https.exception.EnumerateSessionsException;
import net.trollyloki.jicsit.server.https.trustmanager.FingerprintBasedTrustManager;
import net.trollyloki.jicsit.server.https.trustmanager.InsecureTrustManager;
import net.trollyloki.jicsit.server.query.QueryApi;
import net.trollyloki.jicsit.server.query.ServerSubState;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import javax.net.ssl.TrustManager;
import java.net.SocketException;
import java.time.Duration;

/**
 * An interface for the vanilla HTTPS and Lightweight Query API functions.
 */
@NullMarked
public interface ServerApi extends QueryApi, HttpsApi {

    /**
     * Creates a new {@link ServerApi} instance for the vanilla HTTPS and Lightweight Query APIs on a specific server.
     * <p>
     * Authentication can be obtained by calling {@link #setToken(String)} with an API token created by running
     * the {@code server.GenerateAPIToken} command in the dedicated server's console.
     * Alternatively, authentication can also be obtained by calling {@link HttpsApi#passwordlessLogin(PrivilegeLevel)}
     * or {@link HttpsApi#passwordLogin(PrivilegeLevel, String)},
     * but usage of these functions by third-party applications is discouraged by the developers.
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
     * @return new {@link ServerApi} instance
     * @throws IllegalArgumentException if {@code timeout} is non-positive or {@code host}/{@code port} is invalid
     * @throws SocketException          if the query socket could not be opened
     */
    static ServerApi of(String host, int port, @Nullable Duration timeout, @Nullable TrustManager trustManager) throws SocketException {
        return new ServerApiImpl(QueryApi.of(host, port, timeout), HttpsApi.of(host, port, timeout, trustManager));
    }

    /**
     * Retrieves the current state of the server.
     * <p>
     * Meaningful changes in the response to this function are indicated by the {@link ServerSubState#version() version}
     * of {@link ServerSubState#SERVER_GAME_STATE}.
     *
     * @return {@link ServerGameState}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    @Override
    ServerGameState queryServerState();

    /**
     * Retrieves the currently applied and pending server options.
     * <p>
     * Meaningful changes in the response to this function are indicated by the {@link ServerSubState#version() version}
     * of {@link ServerSubState#SERVER_OPTIONS}.
     *
     * @return {@link ServerOptions}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    @Override
    ServerOptions getServerOptions();

    /**
     * Retrieves the currently applied Advanced Game Settings.
     * <p>
     * Meaningful changes in the response to this function are indicated by the {@link ServerSubState#version() version}
     * of {@link ServerSubState#ADVANCED_GAME_SETTINGS}.
     *
     * @return {@link AdvancedGameSettings}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    @Override
    AdvancedGameSettings getAdvancedGameSettings();

    /**
     * Enumerates all session saves available on the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     * <p>
     * Meaningful changes in the response to this function are indicated by the {@link ServerSubState#version() version}
     * of {@link ServerSubState#SAVE_COLLECTION}.
     *
     * @return {@link ServerSessions}
     * @throws EnumerateSessionsException if session saves could not be enumerated
     * @throws ApiException               if an API error occurs
     * @throws RequestException           if an error occurs while sending the request
     */
    @Override
    ServerSessions enumerateSessions();

}
