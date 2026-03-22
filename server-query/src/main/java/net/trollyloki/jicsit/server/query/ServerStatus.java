package net.trollyloki.jicsit.server.query;

import org.jspecify.annotations.NullMarked;

/**
 * A state that a server can be in.
 */
@NullMarked
public enum ServerStatus {

    /**
     * The server is offline.
     * Servers will never send this as a response.
     */
    OFFLINE("Offline", false),

    /**
     * The server is running, but no save is currently loaded.
     */
    IDLE("Idle", true),

    /**
     * The server is currently loading a map.
     * In this state, the HTTPS API is unavailable.
     */
    LOADING("Loading Game", false),

    /**
     * The server is running, and a save is loaded.
     * The server is joinable by players.
     */
    PLAYING("Game Ongoing", true);

    private final String text;
    private final boolean httpsApiAvailable;

    ServerStatus(String text, boolean httpsApiAvailable) {
        this.text = text;
        this.httpsApiAvailable = httpsApiAvailable;
    }

    @Override
    public String toString() {
        return text;
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
