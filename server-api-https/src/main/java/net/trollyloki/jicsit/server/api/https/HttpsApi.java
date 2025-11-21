package net.trollyloki.jicsit.server.api.https;

import net.trollyloki.jicsit.server.api.https.exception.ApiException;
import net.trollyloki.jicsit.server.api.https.exception.CannotRemovePasswordException;
import net.trollyloki.jicsit.server.api.https.exception.DeleteSaveFailedException;
import net.trollyloki.jicsit.server.api.https.exception.DeleteSessionFailedException;
import net.trollyloki.jicsit.server.api.https.exception.EnumerateSessionsException;
import net.trollyloki.jicsit.server.api.https.exception.FileNotFoundException;
import net.trollyloki.jicsit.server.api.https.exception.InvalidSaveException;
import net.trollyloki.jicsit.server.api.https.exception.InvalidTokenException;
import net.trollyloki.jicsit.server.api.https.exception.LoadFailedException;
import net.trollyloki.jicsit.server.api.https.exception.PasswordInUseException;
import net.trollyloki.jicsit.server.api.https.exception.PasswordlessLoginNotPossibleException;
import net.trollyloki.jicsit.server.api.https.exception.SaveFailedException;
import net.trollyloki.jicsit.server.api.https.exception.ServerAlreadyClaimedException;
import net.trollyloki.jicsit.server.api.https.exception.ServerNotClaimedException;
import net.trollyloki.jicsit.server.api.https.exception.SessionNotFoundException;
import net.trollyloki.jicsit.server.api.https.exception.UnsupportedSaveException;
import net.trollyloki.jicsit.server.api.https.exception.WrongPasswordException;

import java.io.InputStream;
import java.util.Map;

/**
 * An interface for the standard HTTPS API functions.
 */
public class HttpsApi {

    private final HttpsApiClient client;

    /**
     * Creates an interface for the standard HTTPS API functions on top of an {@link HttpsApiClient}.
     *
     * @param client underlying client
     */
    public HttpsApi(HttpsApiClient client) {
        this.client = client;
    }

    /**
     * Gets the privilege level granted by the current authentication token.
     * <p>
     * {@link PrivilegeLevel#API_TOKEN} allows the client to execute all functions.
     *
     * @return {@link PrivilegeLevel}, {@link PrivilegeLevel#NOT_AUTHENTICATED} if no token is set
     * @throws IllegalArgumentException if the current authentication token is invalid
     */
    public PrivilegeLevel getPrivilegeLevel() {
        return client.getPrivilegeLevel();
    }

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
    public ServerHealth checkHealth(String customData) {
        return client.request("HealthCheck", Map.of("clientCustomData", customData), ServerHealth.class);
    }

    /**
     * Performs a health check on the server API.
     * This function does not require authentication.
     *
     * @return {@link ServerHealth}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public ServerHealth checkHealth() {
        return checkHealth("");
    }

    /**
     * Verifies that the current authentication token is still valid.
     *
     * @throws InvalidTokenException if the token is invalid
     * @throws ApiException          if an API error occurs
     * @throws RequestException      if an error occurs while sending the request
     */
    public void verifyAuthenticationToken() {
        client.request("VerifyAuthenticationToken");
    }

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
    public void passwordlessLogin(PrivilegeLevel minimumPrivilegeLevel) {
        client.requestToken("PasswordlessLogin", Map.of(
                "minimumPrivilegeLevel", minimumPrivilegeLevel.value()
        ));
    }

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
    public void passwordLogin(PrivilegeLevel minimumPrivilegeLevel, String password) {
        client.requestToken("PasswordLogin", Map.of(
                "minimumPrivilegeLevel", minimumPrivilegeLevel.value(),
                "password", password
        ));
    }

    /**
     * Retrieves the current state of the server.
     *
     * @return {@link ServerGameState}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public ServerGameState queryServerState() {
        record Schema(ServerGameState serverGameState) {
        }
        return client.request("QueryServerState", Schema.class).serverGameState;
    }

    /**
     * Retrieves the currently applied and pending server options.
     *
     * @return {@link ServerOptions}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public ServerOptions getServerOptions() {
        return client.request("GetServerOptions", ServerOptions.class);
    }

    /**
     * Retrieves the currently applied Advanced Game Settings.
     *
     * @return {@link AdvancedGameSettings}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public AdvancedGameSettings getAdvancedGameSettings() {
        return client.request("GetAdvancedGameSettings", AdvancedGameSettings.class);
    }

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
    public void applyAdvancedGameSettings(Map<String, String> settings) {
        client.request("ApplyAdvancedGameSettings", Map.of("appliedAdvancedGameSettings", settings));
    }

    /**
     * Claims the server if it is not claimed.
     * This function requires {@link PrivilegeLevel#INITIAL_ADMIN}, which can be acquired via {@link #passwordlessLogin(PrivilegeLevel)}.
     * <p>
     * The current authentication token is automatically updated to gain {@link PrivilegeLevel#ADMIN} privileges for subsequent requests.
     * {@link #getPrivilegeLevel()} can be used to check the current privilege level.
     *
     * @param name     server name
     * @param password plaintext admin password
     * @throws ServerAlreadyClaimedException if the server is already claimed
     * @throws ApiException                  if an API error occurs
     * @throws RequestException              if an error occurs while sending the request
     */
    public void claimServer(String name, String password) {
        client.requestToken("ClaimServer", Map.of(
                "serverName", name,
                "adminPassword", password
        ));
    }

    /**
     * Renames the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param name new server name
     * @throws ServerNotClaimedException if the server is not claimed yet
     * @throws ApiException              if an API error occurs
     * @throws RequestException          if an error occurs while sending the request
     */
    public void renameServer(String name) {
        client.request("RenameServer", Map.of("serverName", name));
    }

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
    public void setClientPassword(String password) {
        client.request("SetClientPassword", Map.of("password", password));
    }

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
    public void removeClientPassword() {
        setClientPassword("");
    }

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
    public void setAdminPassword(String password) {
        client.requestToken("SetAdminPassword", Map.of("password", password));
    }

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
    public void setAutoLoadSessionName(String sessionName) {
        client.request("SetAutoLoadSessionName", Map.of("sessionName", sessionName));
    }

    /**
     * Runs a console command on the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param command command line to run
     * @return {@link CommandResult}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public CommandResult runCommand(String command) {
        return client.request("RunCommand", Map.of("command", command), CommandResult.class);
    }

    /**
     * Shuts down the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public void shutdownServer() {
        client.request("Shutdown");
    }

    /**
     * Applies new server options to the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param options new server option values
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     * @see ServerOptions
     */
    public void applyServerOptions(Map<String, String> options) {
        client.request("ApplyServerOptions", Map.of("updatedServerOptions", options));
    }

    /**
     * Creates a new session on the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param newGameData parameters needed to create the session
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public void createNewSession(NewGameData newGameData) {
        client.request("CreateNewGame", Map.of("newGameData", newGameData));
    }

    /**
     * Saves the currently loaded session into a new save file. The file name might be changed to satisfy system requirements.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param saveName name of the save file to create (without the extension)
     * @throws SaveFailedException if the game could not be saved
     * @throws ApiException        if an API error occurs
     * @throws RequestException    if an error occurs while sending the request
     */
    public void save(String saveName) {
        client.request("SaveGame", Map.of("saveName", saveName));
    }

    /**
     * Deletes a save file on the server. The file name might be changed to satisfy system requirements.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param saveName name of the save file to delete (without the extension)
     * @throws DeleteSaveFailedException if the file could not be deleted
     * @throws ApiException              if an API error occurs
     * @throws RequestException          if an error occurs while sending the request
     */
    public void deleteSave(String saveName) {
        client.request("DeleteSaveFile", Map.of("saveName", saveName));
    }

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
    public void deleteSession(String sessionName) {
        client.request("DeleteSaveSession", Map.of("sessionName", sessionName));
    }

    /**
     * Enumerates all session saves available on the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @return {@link ServerSessions}
     * @throws EnumerateSessionsException if session saves could not be enumerated
     * @throws ApiException               if an API error occurs
     * @throws RequestException           if an error occurs while sending the request
     */
    public ServerSessions enumerateSessions() {
        return client.request("EnumerateSessions", ServerSessions.class);
    }

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
    public void loadSave(String saveName, boolean enableAdvancedGameSettings) {
        client.request("LoadGame", Map.of("saveName", saveName, "enableAdvancedGameSettings", enableAdvancedGameSettings));
    }

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
    public void uploadSave(InputStream data, String saveName, boolean load, boolean enableAdvancedGameSettings) {
        client.multipartRequest("UploadSaveGame", Map.of(
                "saveName", saveName,
                "loadSaveGame", load,
                "enableAdvancedGameSettings", enableAdvancedGameSettings
        ), "saveGameFile", data);
    }

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
    public InputStream downloadSave(String saveName) {
        return client.requestRaw("DownloadSaveGame", Map.of("saveName", saveName));
    }

}
