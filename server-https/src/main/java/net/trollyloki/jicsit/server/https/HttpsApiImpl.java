package net.trollyloki.jicsit.server.https;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.InputStream;
import java.util.Map;

@NullMarked
class HttpsApiImpl implements HttpsApi {

    private final HttpsClient client;

    public HttpsApiImpl(HttpsClient client) {
        this.client = client;
    }

    @Override
    public void setToken(@Nullable String token) {
        client.setToken(token);
    }

    @Override
    public PrivilegeLevel getPrivilegeLevel() {
        String token = client.getToken();
        if (token == null)
            return PrivilegeLevel.NOT_AUTHENTICATED;
        else
            return PrivilegeLevel.ofToken(token);
    }

    @Override
    public ServerHealth checkHealth(String customData) {
        return client.request("HealthCheck", Map.of("clientCustomData", customData), ServerHealth.class);
    }

    @Override
    public ServerHealth checkHealth() {
        return checkHealth("");
    }

    @Override
    public void verifyAuthenticationToken() {
        client.request("VerifyAuthenticationToken", null);
    }

    @Override
    public void passwordlessLogin(PrivilegeLevel minimumPrivilegeLevel) {
        client.requestToken("PasswordlessLogin", Map.of(
                "minimumPrivilegeLevel", minimumPrivilegeLevel.value()
        ));
    }

    @Override
    public void passwordLogin(PrivilegeLevel minimumPrivilegeLevel, String password) {
        client.requestToken("PasswordLogin", Map.of(
                "minimumPrivilegeLevel", minimumPrivilegeLevel.value(),
                "password", password
        ));
    }

    @Override
    public ServerGameState queryServerState() {
        record Schema(ServerGameState serverGameState) {
        }
        return client.request("QueryServerState", null, Schema.class).serverGameState;
    }

    @Override
    public ServerOptions getServerOptions() {
        return client.request("GetServerOptions", null, ServerOptions.class);
    }

    @Override
    public AdvancedGameSettings getAdvancedGameSettings() {
        return client.request("GetAdvancedGameSettings", null, AdvancedGameSettings.class);
    }

    @Override
    public void applyAdvancedGameSettings(Map<String, String> settings) {
        client.request("ApplyAdvancedGameSettings", Map.of("appliedAdvancedGameSettings", settings));
    }

    @Override
    public void claimServer(String name, String password) {
        client.requestToken("ClaimServer", Map.of(
                "serverName", name,
                "adminPassword", password
        ));
    }

    @Override
    public void renameServer(String name) {
        client.request("RenameServer", Map.of("serverName", name));
    }

    @Override
    public void setClientPassword(String password) {
        client.request("SetClientPassword", Map.of("password", password));
    }

    @Override
    public void removeClientPassword() {
        setClientPassword("");
    }

    @Override
    public void setAdminPassword(String password) {
        client.requestToken("SetAdminPassword", Map.of("password", password));
    }

    @Override
    public void setAutoLoadSessionName(String sessionName) {
        client.request("SetAutoLoadSessionName", Map.of("sessionName", sessionName));
    }

    @Override
    public CommandResult runCommand(String command) {
        return client.request("RunCommand", Map.of("command", command), CommandResult.class);
    }

    @Override
    public void shutdownServer() {
        client.request("Shutdown", null);
    }

    @Override
    public void applyServerOptions(Map<String, String> options) {
        client.request("ApplyServerOptions", Map.of("updatedServerOptions", options));
    }

    @Override
    public void createNewSession(NewGameData newGameData) {
        client.request("CreateNewGame", Map.of("newGameData", newGameData));
    }

    @Override
    public void save(String saveName) {
        client.request("SaveGame", Map.of("saveName", saveName));
    }

    @Override
    public void deleteSave(String saveName) {
        client.request("DeleteSaveFile", Map.of("saveName", saveName));
    }

    @Override
    public void deleteSession(String sessionName) {
        client.request("DeleteSaveSession", Map.of("sessionName", sessionName));
    }

    @Override
    public ServerSessions enumerateSessions() {
        return client.request("EnumerateSessions", null, ServerSessions.class);
    }

    @Override
    public void loadSave(String saveName, boolean enableAdvancedGameSettings) {
        client.request("LoadGame", Map.of("saveName", saveName, "enableAdvancedGameSettings", enableAdvancedGameSettings));
    }

    @Override
    public void uploadSave(InputStream data, String saveName, boolean load, boolean enableAdvancedGameSettings) {
        client.multipartRequest("UploadSaveGame", Map.of(
                "saveName", saveName,
                "loadSaveGame", load,
                "enableAdvancedGameSettings", enableAdvancedGameSettings
        ), "saveGameFile", data);
    }

    @Override
    public InputStream downloadSave(String saveName) {
        return client.requestRaw("DownloadSaveGame", Map.of("saveName", saveName));
    }

}
