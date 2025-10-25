package net.trollyloki.jicsit.server.api.query;

/**
 * A state that a server can be in.
 */
public enum ServerStatus {

    /**
     * The server is offline.
     * Servers will never send this as a response.
     */
    OFFLINE(false),

    /**
     * The server is running, but no save is currently loaded.
     */
    IDLE(true),

    /**
     * The server is currently loading a map.
     * In this state, the HTTPS API is unavailable.
     */
    LOADING(false),

    /**
     * The server is running, and a save is loaded.
     * The server is joinable by players.
     */
    PLAYING(true);

    private final boolean httpsApiAvailable;

    ServerStatus(boolean httpsApiAvailable) {
        this.httpsApiAvailable = httpsApiAvailable;
    }

    /**
     * Checks if the HTTPS API is available while the server is in this state.
     *
     * @return {@code true} if the HTTPS API is available, or {@code false} if it is unavailable
     */
    public boolean isHttpsApiAvailable() {
        return httpsApiAvailable;
    }

}
