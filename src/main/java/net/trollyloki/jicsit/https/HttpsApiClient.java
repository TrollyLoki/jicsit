package net.trollyloki.jicsit.https;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.trollyloki.jicsit.https.exception.ApiException;
import net.trollyloki.jicsit.https.exception.CannotRemovePasswordException;
import net.trollyloki.jicsit.https.exception.DeleteSaveFailedException;
import net.trollyloki.jicsit.https.exception.DeleteSessionFailedException;
import net.trollyloki.jicsit.https.exception.EnumerateSessionsException;
import net.trollyloki.jicsit.https.exception.FileNotFoundException;
import net.trollyloki.jicsit.https.exception.InvalidSaveException;
import net.trollyloki.jicsit.https.exception.InvalidTokenException;
import net.trollyloki.jicsit.https.exception.LoadFailedException;
import net.trollyloki.jicsit.https.exception.PasswordInUseException;
import net.trollyloki.jicsit.https.exception.PasswordlessLoginNotPossibleException;
import net.trollyloki.jicsit.https.exception.SaveFailedException;
import net.trollyloki.jicsit.https.exception.ServerAlreadyClaimedException;
import net.trollyloki.jicsit.https.exception.ServerNotClaimedException;
import net.trollyloki.jicsit.https.exception.SessionNotFoundException;
import net.trollyloki.jicsit.https.exception.UnsupportedSaveException;
import net.trollyloki.jicsit.https.exception.WrongPasswordException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * A client for the dedicated server HTTPS API.
 */
public class HttpsApiClient {

    private final RestClient client;
    private String token;

    private record ResponseSchema<T>(T data, String errorCode, String errorMessage, Map<String, Object> errorData) {

        private ApiException createApiException() {
            return ApiException.createApiException(errorCode, errorMessage, errorData);
        }

    }

    /**
     * Creates a new client.
     *
     * @param host  server host name
     * @param port  server port
     * @param token API token, or {@code null} for an unauthenticated client
     * @throws URISyntaxException if the constructed HTTPS URI is invalid
     */
    public HttpsApiClient(String host, int port, String token) throws URISyntaxException {
        setToken(token);

        RestClient.Builder builder = RestClient.builder().requestInterceptor((request, body, execution) -> {
            // without this interceptor the requests don't include a content length header for some reason
            return execution.execute(request, body);
        });

        // certificate handling
        //TODO: Add some sort of fingerprint checking like the game client has
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new InsecureTrustManager()}, null);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        builder.requestFactory(new JdkClientHttpRequestFactory(HttpClient.newBuilder().sslContext(sslContext).build()));

        // error handling
        ObjectMapper jsonMapper = Jackson2ObjectMapperBuilder.json().build();
        builder.defaultStatusHandler(HttpStatusCode::isError, (httpRequest, httpResponse) -> {
            throw jsonMapper.readValue(httpResponse.getBody(), ResponseSchema.class).createApiException();
        });

        // server configuration
        builder.baseUrl(new URI("https", null, host, port, "/api/v1", null, null));

        this.client = builder.build();
    }

    /**
     * Creates a new (unauthenticated) client.
     *
     * @param host server host name
     * @param port server port
     * @throws URISyntaxException if the constructed HTTPS URI is invalid
     */
    public HttpsApiClient(String host, int port) throws URISyntaxException {
        this(host, port, null);
    }

    /**
     * Sets the current authentication token used for all requests.
     *
     * @param token authentication token, or {@code null} to make unauthenticated requests
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the privilege level granted by the current authentication token.
     * <p>
     * {@link PrivilegeLevel#API_TOKEN} allows the client to execute all functions.
     *
     * @return privilege level, if no token is set then {@link PrivilegeLevel#NOT_AUTHENTICATED}
     * @throws IllegalArgumentException if the current authentication token is invalid
     */
    public PrivilegeLevel getPrivilegeLevel() {
        if (token == null) return PrivilegeLevel.NOT_AUTHENTICATED;
        return PrivilegeLevel.ofToken(token);
    }

    private Map<String, Object> createRequestBody(String function, Object functionData) {
        if (functionData == null) {
            return Map.of("function", function);
        } else {
            return Map.of("function", function, "data", functionData);
        }
    }

    private RestClient.RequestBodySpec createRequest() {
        RestClient.RequestBodySpec request = client.post();
        if (token != null) {
            request.headers(headers -> headers.setBearerAuth(token));
        }
        return request;
    }

    private RestClient.RequestBodySpec createRequest(String function, Object functionData) {
        return createRequest().contentType(MediaType.APPLICATION_JSON).body(createRequestBody(function, functionData));
    }

    private RestClient.RequestBodySpec createMultipartRequest(String function, Object functionData, String partName, Object partData) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        parts.add("data", createRequestBody(function, functionData));
        parts.add(partName, partData);

        return createRequest().contentType(MediaType.MULTIPART_FORM_DATA).body(parts);
    }

    private <T> T readResponse(RestClient.RequestBodySpec request, ParameterizedTypeReference<T> bodyType) {
        try {
            return request.retrieve().body(bodyType);
        } catch (RestClientException e) {
            throw new RequestException(e.getMessage(), e.getCause());
        }
    }

    private byte[] rawResponse(RestClient.RequestBodySpec request) {
        return readResponse(request, ParameterizedTypeReference.forType(byte[].class));
    }

    private <R> R parseResponse(RestClient.RequestBodySpec request, Class<R> responseDataType) {
        ParameterizedType responseType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{responseDataType};
            }

            @Override
            public Type getRawType() {
                return ResponseSchema.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        ResponseSchema<R> response = readResponse(request, ParameterizedTypeReference.forType(responseType));
        if (response == null) return null;

        // the server sometimes returns errors with a success status code for some reason, so catch that
        if (response.errorCode() != null) { // errorCode is the only required property in an error response
            throw response.createApiException();
        }

        return response.data();
    }

    /**
     * Sends a request to the server and reads the response as raw data.
     *
     * @param function     name of the API function to execute
     * @param functionData data for the function, or {@code null} to not include data in the request
     * @return raw data
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    protected byte[] requestRaw(String function, Object functionData) {
        return rawResponse(createRequest(function, functionData));
    }

    /**
     * Sends a request to the server with no data for the function and reads the response as raw data.
     *
     * @param function name of the API function to execute
     * @return raw data
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    protected byte[] requestRaw(String function) {
        return requestRaw(function, null);
    }

    /**
     * Sends a request to the server.
     *
     * @param function         name of the API function to execute
     * @param functionData     data for the function, or {@code null} to not include data in the request
     * @param responseDataType class to parse the response data into
     * @param <R>              type to return
     * @return response data, or {@code null} if there was none
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    protected <R> R request(String function, Object functionData, Class<R> responseDataType) {
        return parseResponse(createRequest(function, functionData), responseDataType);
    }

    /**
     * Sends a request to the server with no data for the function.
     *
     * @param function         name of the API function to execute
     * @param responseDataType class to parse the response data into
     * @param <R>              type to return
     * @return response data, or {@code null} if there was none
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    protected <R> R request(String function, Class<R> responseDataType) {
        return request(function, null, responseDataType);
    }

    /**
     * Sends a request to the server expecting no response data.
     *
     * @param function     name of the API function to execute
     * @param functionData data for the function, or {@code null} to not include data in the request
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    protected void request(String function, Object functionData) {
        request(function, functionData, Void.class);
    }

    /**
     * Sends a request to the server with no data for the function and also expecting no response data.
     *
     * @param function name of the API function to execute
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    protected void request(String function) {
        request(function, (Object) null);
    }

    /**
     * Sends a multipart request to the server.
     *
     * @param function     name of the API function to execute
     * @param functionData data for the function, or {@code null} to not include data in the request
     * @param partName     name of the other part of the request
     * @param partData     raw data for the other part of the request
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    protected void multipartRequest(String function, Object functionData, String partName, Object partData) {
        parseResponse(createMultipartRequest(function, functionData, partName, partData), Void.class);
    }

    /**
     * Sends a multipart request to the server with no data for the function.
     *
     * @param function name of the API function to execute
     * @param partName name of the other part of the request
     * @param partData raw data for the other part of the request
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    protected void multipartRequest(String function, String partName, Object partData) {
        multipartRequest(function, null, partName, partData);
    }

    private void requestToken(String function, Object functionData) {
        record Schema(String authenticationToken) {
        }
        String newToken = request(function, functionData, Schema.class).authenticationToken;
        if (newToken != null && !newToken.isEmpty()) {
            token = newToken;
        }
    }

    /**
     * Performs a health check on the server API.
     * This function does not require authentication.
     * <p>
     * Also allows passing custom data between the server and the client.
     *
     * @param customData client custom data (ignored by vanilla servers)
     * @return server {@link ServerHealth health}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public ServerHealth checkHealth(String customData) {
        return request("HealthCheck", Map.of("clientCustomData", customData), ServerHealth.class);
    }

    /**
     * Performs a health check on the server API.
     * This function does not require authentication.
     *
     * @return server {@link ServerHealth health}
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
        request("VerifyAuthenticationToken");
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
        requestToken("PasswordlessLogin", Map.of(
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
        requestToken("PasswordLogin", Map.of(
                "minimumPrivilegeLevel", minimumPrivilegeLevel.value(),
                "password", password
        ));
    }

    /**
     * Retrieves the current state of the server.
     *
     * @return {@link ServerGameState server game state}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public ServerGameState queryServerState() {
        record Schema(ServerGameState serverGameState) {
        }
        return request("QueryServerState", Schema.class).serverGameState;
    }

    /**
     * Retrieves the currently applied and pending server options.
     *
     * @return {@link ServerOptions server options}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public ServerOptions getServerOptions() {
        return request("GetServerOptions", ServerOptions.class);
    }

    /**
     * Retrieves the currently applied Advanced Game Settings.
     *
     * @return {@link AdvancedGameSettings}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public AdvancedGameSettings getAdvancedGameSettings() {
        return request("GetAdvancedGameSettings", AdvancedGameSettings.class);
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
        request("ApplyAdvancedGameSettings", Map.of("appliedAdvancedGameSettings", settings));
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
        requestToken("ClaimServer", Map.of(
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
        request("RenameServer", Map.of("serverName", name));
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
        request("SetClientPassword", Map.of("password", password));
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
        requestToken("SetAdminPassword", Map.of("password", password));
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
        request("SetAutoLoadSessionName", Map.of("sessionName", sessionName));
    }

    /**
     * Runs a console command on the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param command command line to run
     * @return {@link CommandResult command result}
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public CommandResult runCommand(String command) {
        return request("RunCommand", Map.of("command", command), CommandResult.class);
    }

    /**
     * Shuts down the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public void shutdownServer() {
        request("Shutdown");
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
        request("ApplyServerOptions", Map.of("updatedServerOptions", options));
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
        request("CreateNewGame", Map.of("newGameData", newGameData));
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
        request("SaveGame", Map.of("saveName", saveName));
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
        request("DeleteSaveFile", Map.of("saveName", saveName));
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
        request("DeleteSaveSession", Map.of("sessionName", sessionName));
    }

    /**
     * Enumerates all session saves available on the server.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @return {@link ServerSessions server sessions}
     * @throws EnumerateSessionsException if session saves could not be enumerated
     * @throws ApiException               if an API error occurs
     * @throws RequestException           if an error occurs while sending the request
     */
    public ServerSessions enumerateSessions() {
        return request("EnumerateSessions", ServerSessions.class);
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
        request("LoadGame", Map.of("saveName", saveName, "enableAdvancedGameSettings", enableAdvancedGameSettings));
    }

    /**
     * Uploads a save file to the server, and optionally loads it immediately.
     * This function requires {@link PrivilegeLevel#ADMIN}.
     *
     * @param data                       save file data
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
    public void uploadSave(byte[] data, String saveName, boolean load, boolean enableAdvancedGameSettings) {
        multipartRequest("UploadSaveGame", Map.of(
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
     * @return save file data
     * @throws FileNotFoundException if the save file could not be found
     * @throws ApiException          if an API error occurs
     * @throws RequestException      if an error occurs while sending the request
     */
    public byte[] downloadSave(String saveName) {
        return requestRaw("DownloadSaveGame", Map.of("saveName", saveName));
    }

}
