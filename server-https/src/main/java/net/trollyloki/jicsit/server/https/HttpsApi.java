package net.trollyloki.jicsit.server.https;

import net.trollyloki.jicsit.server.https.exception.ApiException;
import net.trollyloki.jicsit.server.https.exception.CannotRemovePasswordException;
import net.trollyloki.jicsit.server.https.exception.DeleteSaveFailedException;
import net.trollyloki.jicsit.server.https.exception.DeleteSessionFailedException;
import net.trollyloki.jicsit.server.https.exception.EnumerateSessionsException;
import net.trollyloki.jicsit.server.https.exception.FileNotFoundException;
import net.trollyloki.jicsit.server.https.exception.InvalidSaveException;
import net.trollyloki.jicsit.server.https.exception.InvalidTokenException;
import net.trollyloki.jicsit.server.https.exception.LoadFailedException;
import net.trollyloki.jicsit.server.https.exception.PasswordInUseException;
import net.trollyloki.jicsit.server.https.exception.PasswordlessLoginNotPossibleException;
import net.trollyloki.jicsit.server.https.exception.SaveFailedException;
import net.trollyloki.jicsit.server.https.exception.ServerAlreadyClaimedException;
import net.trollyloki.jicsit.server.https.exception.ServerNotClaimedException;
import net.trollyloki.jicsit.server.https.exception.SessionNotFoundException;
import net.trollyloki.jicsit.server.https.exception.UnsupportedSaveException;
import net.trollyloki.jicsit.server.https.exception.WrongPasswordException;
import net.trollyloki.jicsit.server.https.trustmanager.FingerprintBasedTrustManager;
import net.trollyloki.jicsit.server.https.trustmanager.InsecureTrustManager;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import javax.net.ssl.TrustManager;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;

/**
 * An interface for the vanilla HTTPS API functions.
 */
@NullMarked
public interface HttpsApi {

    /**
     * Creates a new {@link HttpsApi} instance for the vanilla HTTPS API on a specific server.
     * <p>
     * Authentication can be obtained by calling {@link #setToken(String)} with an API token created by running
     * the {@code server.GenerateAPIToken} command in the dedicated server's console.
     * Alternatively, authentication can also be obtained by calling {@link HttpsApi#passwordlessLogin(PrivilegeLevel)}
     * or {@link HttpsApi#passwordLogin(PrivilegeLevel, String)},
     * but usage of these functions by third-party applications is discouraged by the developers.
     * <p>
     * Note that the returned {@link HttpsApi} will
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
     * @param timeout      duration to allow the underlying connection to be established, or {@code null} to wait indefinitely
     * @param trustManager custom trust manager to use, or {@code null} to use the default trust manager
     * @return new {@link HttpsApi} instance
     * @throws IllegalArgumentException if {@code timeout} is non-positive or {@code host}/{@code port} is invalid
     * @see HttpsClient#HttpsClient(String, int, Duration, TrustManager)
     */
    static HttpsApi of(String host, int port, @Nullable Duration timeout, @Nullable TrustManager trustManager) {
        HttpsClient client = new HttpsClient(host, port, timeout, trustManager);
        return of(client);
    }

    /**
     * Creates a new {@link HttpsApi} instance for the vanilla HTTPS API on top of a {@link HttpsClient}.
     *
     * @param client underlying client
     * @return new {@link HttpsApi} instance
     */
    static HttpsApi of(HttpsClient client) {
        return new HttpsApiImpl(client);
    }

    /**
     * Sets the current authentication token.
     * An API authentication token can be obtained by running
     * the {@code server.GenerateAPIToken} command in the dedicated server's console.
     *
     * @param token authentication token, or {@code null} to make unauthenticated requests
     */
    void setToken(@Nullable String token);

    /**
     * Gets the privilege level granted by the current authentication token.
     * <p>
     * {@link PrivilegeLevel#API_TOKEN} allows the client to execute all functions.
     *
     * @return {@link PrivilegeLevel}, {@link PrivilegeLevel#NOT_AUTHENTICATED} if no token is set
     * @throws IllegalArgumentException if the current authentication token is invalid
     */
    PrivilegeLevel getPrivilegeLevel();

    /**
     * Performs a health check on the server API.
     * This function does not require authentication.
     * <p>
     * Also allows passing custom data between the server and the client.
     *
     * @param customData client custom data (ignored by vanilla servers)
     * @return {@link ServerHealth}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    ServerHealth checkHealth(String customData);

    /**
     * Performs a health check on the server API.
     * This function does not require authentication.
     *
     * @return {@link ServerHealth}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    ServerHealth checkHealth();

    /**
     * Verifies that the current authentication token is still valid.
     *
     * @throws InvalidTokenException if the token is invalid
     * @throws ApiException          if an API error occurs
     * @throws RequestException      if an error occurs while sending the request
     */
    void verifyAuthenticationToken();

    /**
     * Attempts to perform a passwordless login to the server as a player.
     * This function does not require authentication.
     * <p>
     * This is only possible if the server is not claimed or the player password is not set for the server.
     * <p>
     * Upon success, the current authentication token is updated for use by subsequent requests.
     * {@link #getPrivilegeLevel()} can be used to check the current privilege level.
     *
     * @param minimumPrivilegeLevel minimum privilege level to attempt to acquire by logging in
     * @throws PasswordlessLoginNotPossibleException if passwordless login is not currently possible
     * @throws ApiException                          if an API error occurs
     * @throws RequestException                      if an error occurs while sending the request
     * @see #removeClientPassword()
     * @see #getPrivilegeLevel()
     */
    void passwordlessLogin(PrivilegeLevel minimumPrivilegeLevel);

    /**
     * Attempts to log in to the server as a player using either the admin password or client password.
     * This function does not require authentication.
     * <p>
     * Upon success, the current authentication token is updated for use by subsequent requests.
     * {@link #getPrivilegeLevel()} can be used to check the current privilege level.
     *
     * @param minimumPrivilegeLevel minimum privilege level to attempt to acquire by logging in
     * @param password              plaintext password
     * @throws WrongPasswordException if the password is incorrect
     * @throws ApiException           if an API error occurs
     * @throws RequestException       if an error occurs while sending the request
     * @see #setClientPassword(String)
     * @see #setAdminPassword(String)
     */
    void passwordLogin(PrivilegeLevel minimumPrivilegeLevel, String password);

    /**
     * Retrieves the current state of the server.
     *
     * @return {@link ServerGameState}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    ServerGameState queryServerState();

    /**
     * Retrieves the currently applied and pending server options.
     *
     * @return {@link ServerOptions}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    ServerOptions getServerOptions();

    /**
     * Retrieves the currently applied Advanced Game Settings.
     *
     * @return {@link AdvancedGameSettings}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    AdvancedGameSettings getAdvancedGameSettings();

    /**
     * Applies new Advanced Game Settings values.
     * <p>
     * This will automatically enable Advanced Game Settings for the currently loaded save if they are not enabled already.
     *
     * @param settings new Advanced Game Settings values
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     * @see AdvancedGameSettings
     */
    void applyAdvancedGameSettings(Map<String, String> settings);

    /**
     * Claims the server if it is not claimed.
     * This function requires {@link PrivilegeLevel#INITIAL_ADMIN}, which can be acquired via {@link #passwordlessLogin(PrivilegeLevel)}.
     * <p>
     * The current authentication token is automatically updated to gain {@link PrivilegeLevel#ADMIN} privileges for subsequent requests.
     * {@link #getPrivilegeLevel()} can be used to check the current privilege level.
     * <p>
     * <strong>Note:</strong> The server truncates the provided name if it is longer than 32 characters.
     *
     * @param name     server name
     * @param password plaintext admin password
     * @throws ServerAlreadyClaimedException if the server is already claimed
     * @throws ApiException                  if an API error occurs
     * @throws RequestException              if an error occurs while sending the request
     */
    void claimServer(String name, String password);

    /**
     * Renames the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     * <p>
     * <strong>Note:</strong> The server truncates the provided name if it is longer than 32 characters.
     *
     * @param name new server name
     * @throws ServerNotClaimedException if the server is not claimed yet
     * @throws ApiException              if an API error occurs
     * @throws RequestException          if an error occurs while sending the request
     */
    void renameServer(String name);

    /**
     * Updates the currently set player password.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     * <p>
     * This will invalidate all previously issued Client authentication tokens.
     *
     * @param password plaintext password, or an empty string to remove the password
     * @throws ServerNotClaimedException if the server is not claimed yet
     * @throws PasswordInUseException    if {@code password} is already used as the admin password
     * @throws ApiException              if an API error occurs
     * @throws RequestException          if an error occurs while sending the request
     */
    void setClientPassword(String password);

    /**
     * Removes the currently set player password, allowing anyone to join the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     * <p>
     * This will invalidate all previously issued Client authentication tokens.
     *
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     * @see #setClientPassword(String)
     */
    void removeClientPassword();

    /**
     * Updates the currently set admin password.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     * <p>
     * This will invalidate all previously issued Client <strong>and</strong> Admin authentication tokens.
     * <p>
     * If necessary, the current authentication token is automatically updated to maintain privileges for subsequent requests.
     * {@link #getPrivilegeLevel()} can be used to check the current privilege level.
     *
     * @param password plaintext password
     * @throws ServerNotClaimedException     if the server is not claimed yet
     * @throws CannotRemovePasswordException if {@code password} is empty
     * @throws PasswordInUseException        if {@code password} is already used as the player password
     * @throws ApiException                  if an API error occurs
     * @throws RequestException              if an error occurs while sending the request
     */
    void setAdminPassword(String password);

    /**
     * Updates the session that the server will automatically load on startup.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     * <p>
     * This does not change the currently loaded session.
     *
     * @param sessionName name of the session to automatically load on server startup
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    void setAutoLoadSessionName(String sessionName);

    /**
     * Runs a console command on the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param command command line to run
     * @return {@link CommandResult}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    CommandResult runCommand(String command);

    /**
     * Shuts down the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    void shutdownServer();

    /**
     * Applies new server options to the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param options new server option values
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     * @see ServerOptions
     */
    void applyServerOptions(Map<String, String> options);

    /**
     * Creates a new session on the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param newGameData parameters needed to create the session
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    void createNewSession(NewGameData newGameData);

    /**
     * Saves the currently loaded session into a new save file. The file name might be changed to satisfy system requirements.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param saveName name of the save file to create (without the extension)
     * @throws SaveFailedException if the game could not be saved
     * @throws ApiException        if an API error occurs
     * @throws RequestException    if an error occurs while sending the request
     */
    void save(String saveName);

    /**
     * Deletes a save file on the server. The file name might be changed to satisfy system requirements.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param saveName name of the save file to delete (without the extension)
     * @throws DeleteSaveFailedException if the file could not be deleted
     * @throws ApiException              if an API error occurs
     * @throws RequestException          if an error occurs while sending the request
     */
    void deleteSave(String saveName);

    /**
     * Deletes all save files on the server belonging to a specific session.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param sessionName name of the session to delete
     * @throws SessionNotFoundException     if there were no files belonging to the specified session
     * @throws DeleteSessionFailedException if the files could not be deleted
     * @throws ApiException                 if an API error occurs
     * @throws RequestException             if an error occurs while sending the request
     */
    void deleteSession(String sessionName);

    /**
     * Enumerates all session saves available on the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @return {@link ServerSessions}
     * @throws EnumerateSessionsException if session saves could not be enumerated
     * @throws ApiException               if an API error occurs
     * @throws RequestException           if an error occurs while sending the request
     */
    ServerSessions enumerateSessions();

    /**
     * Loads a save file on the server, optionally with Advanced Game Settings enabled.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param saveName                   name of the save file to load (without the extension)
     * @param enableAdvancedGameSettings {@code true} if the file should be loaded with Advanced Game Settings enabled
     * @throws LoadFailedException if the save file could not be loaded
     * @throws ApiException        if an API error occurs
     * @throws RequestException    if an error occurs while sending the request
     */
    void loadSave(String saveName, boolean enableAdvancedGameSettings);

    /**
     * Uploads a save file to the server, and optionally loads it immediately.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param data                       input stream containing the save file data
     * @param saveName                   name of the save file to create (without the extension)
     * @param load                       {@code true} if the file should be loaded immediately
     * @param enableAdvancedGameSettings {@code true} if the file should be loaded with Advanced Game Settings enabled
     * @throws InvalidSaveException     if the save file is invalid
     * @throws UnsupportedSaveException if the save file is not supported by the server
     * @throws SaveFailedException      if the file could not be saved
     * @throws LoadFailedException      if the save file could not be loaded
     * @throws ApiException             if an API error occurs
     * @throws RequestException         if an error occurs while sending the request
     */
    void uploadSave(InputStream data, String saveName, boolean load, boolean enableAdvancedGameSettings);

    /**
     * Downloads a save file from the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param saveName name of the save file to download (without the extension)
     * @return input stream containing the save file data
     * @throws FileNotFoundException if the save file could not be found
     * @throws ApiException          if an API error occurs
     * @throws RequestException      if an error occurs while sending the request
     */
    InputStream downloadSave(String saveName);

}
