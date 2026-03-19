package net.trollyloki.jicsit.server;

import net.trollyloki.jicsit.server.https.AdvancedGameSettings;
import net.trollyloki.jicsit.server.https.CommandResult;
import net.trollyloki.jicsit.server.https.HttpsApi;
import net.trollyloki.jicsit.server.https.NewGameData;
import net.trollyloki.jicsit.server.https.PrivilegeLevel;
import net.trollyloki.jicsit.server.https.ServerGameState;
import net.trollyloki.jicsit.server.https.ServerHealth;
import net.trollyloki.jicsit.server.https.ServerOptions;
import net.trollyloki.jicsit.server.https.ServerSessions;
import net.trollyloki.jicsit.server.query.QueryApi;
import net.trollyloki.jicsit.server.query.ServerState;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@NullMarked
class ServerApiImpl implements ServerApi {

    private final QueryApi queryApi;
    private final HttpsApi httpsApi;

    public ServerApiImpl(QueryApi queryApi, HttpsApi httpsApi) {
        this.queryApi = queryApi;
        this.httpsApi = httpsApi;
    }

    @Override
    public void close() {
        queryApi.close();
    }

    @Override
    public ServerState pollServerState(long cookie) throws IOException {
        return queryApi.pollServerState(cookie);
    }

    @Override
    public ServerState pollServerState() throws IOException {
        return queryApi.pollServerState();
    }

    @Override
    public void setToken(@Nullable String token) {
        httpsApi.setToken(token);
    }

    @Override
    public PrivilegeLevel getPrivilegeLevel() {
        return httpsApi.getPrivilegeLevel();
    }

    @Override
    public ServerHealth checkHealth(String customData) {
        return httpsApi.checkHealth(customData);
    }

    @Override
    public ServerHealth checkHealth() {
        return httpsApi.checkHealth();
    }

    @Override
    public void verifyAuthenticationToken() {
        httpsApi.verifyAuthenticationToken();
    }

    @Override
    public void passwordlessLogin(PrivilegeLevel minimumPrivilegeLevel) {
        httpsApi.passwordlessLogin(minimumPrivilegeLevel);
    }

    @Override
    public void passwordLogin(PrivilegeLevel minimumPrivilegeLevel, String password) {
        httpsApi.passwordLogin(minimumPrivilegeLevel, password);
    }

    @Override
    public ServerGameState queryServerState() {
        //TODO: Check substate version for changes
        return httpsApi.queryServerState();
    }

    @Override
    public ServerOptions getServerOptions() {
        //TODO: Check substate version for changes
        return httpsApi.getServerOptions();
    }

    @Override
    public AdvancedGameSettings getAdvancedGameSettings() {
        //TODO: Check substate version for changes
        return httpsApi.getAdvancedGameSettings();
    }

    @Override
    public void applyAdvancedGameSettings(Map<String, String> settings) {
        httpsApi.applyAdvancedGameSettings(settings);
    }

    @Override
    public void claimServer(String name, String password) {
        httpsApi.claimServer(name, password);
    }

    @Override
    public void renameServer(String name) {
        httpsApi.renameServer(name);
    }

    @Override
    public void setClientPassword(String password) {
        httpsApi.setClientPassword(password);
    }

    @Override
    public void removeClientPassword() {
        httpsApi.removeClientPassword();
    }

    @Override
    public void setAdminPassword(String password) {
        httpsApi.setAdminPassword(password);
    }

    @Override
    public void setAutoLoadSessionName(String sessionName) {
        httpsApi.setAutoLoadSessionName(sessionName);
    }

    @Override
    public CommandResult runCommand(String command) {
        return httpsApi.runCommand(command);
    }

    @Override
    public void shutdownServer() {
        httpsApi.shutdownServer();
    }

    @Override
    public void applyServerOptions(Map<String, String> options) {
        httpsApi.applyServerOptions(options);
    }

    @Override
    public void createNewSession(NewGameData newGameData) {
        httpsApi.createNewSession(newGameData);
    }

    @Override
    public void save(String saveName) {
        httpsApi.save(saveName);
    }

    @Override
    public void deleteSave(String saveName) {
        httpsApi.deleteSave(saveName);
    }

    @Override
    public void deleteSession(String sessionName) {
        httpsApi.deleteSession(sessionName);
    }

    @Override
    public ServerSessions enumerateSessions() {
        //TODO: Check substate version for changes
        return httpsApi.enumerateSessions();
    }

    @Override
    public void loadSave(String saveName, boolean enableAdvancedGameSettings) {
        httpsApi.loadSave(saveName, enableAdvancedGameSettings);
    }

    @Override
    public void uploadSave(InputStream data, String saveName, boolean load, boolean enableAdvancedGameSettings) {
        httpsApi.uploadSave(data, saveName, load, enableAdvancedGameSettings);
    }

    @Override
    public InputStream downloadSave(String saveName) {
        return httpsApi.downloadSave(saveName);
    }

}
