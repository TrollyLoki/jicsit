package net.trollyloki.jicsit.server.api.query;

/**
 * A state that a server can be in.
 */
public enum ServerStatus {

    /**
     * The server is offline.
     * Servers will never send this as a response.
     */
    OFFLINE,

    /**
     * The server is running, but no save is currently loaded.
     */
    IDLE,

    /**
     * The server is currently loading a map.
     * In this state, the HTTPS API is unavailable.
     */
    LOADING,

    /**
     * The server is running, and a save is loaded.
     * The server is joinable by players.
     */
    PLAYING

}
