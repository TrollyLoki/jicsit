package net.trollyloki.jicsit.server.query;

import org.jspecify.annotations.NullMarked;

/**
 * A sequential counter that is incremented by the server each time a state of the relevant system changes.
 * <p>
 * This allows determining a set of data that the API client needs to refresh when the server changes it,
 * without having to continuously poll the HTTPS API.
 * <p>
 * Vanilla servers support the following substate IDs:
 * <ul>
 *     <li>{@link #SERVER_GAME_STATE}
 *     <li>{@link #SERVER_OPTIONS}
 *     <li>{@link #ADVANCED_GAME_SETTINGS}
 *     <li>{@link #SAVE_COLLECTION}
 * </ul>
 *
 * @param id      ID of the substate being changed
 * @param version current changelist of the substate
 */
@NullMarked
public record ServerSubState(byte id, short version) {

    /**
     * Game state of the server.
     * Maps to QueryServerState HTTPS API function.
     */
    public static final byte SERVER_GAME_STATE = 0;

    /**
     * Global options set on the server.
     * Maps to GetServerOptions HTTPS API function.
     */
    public static final byte SERVER_OPTIONS = 1;

    /**
     * Advanced Game Settings in the currently loaded session.
     * Maps to GetAdvancedGameSettings HTTPS API function.
     */
    public static final byte ADVANCED_GAME_SETTINGS = 2;

    /**
     * List of saves available on the server for loading/downloading.
     * Maps to EnumerateSessions HTTPS API function.
     */
    public static final byte SAVE_COLLECTION = 3;

}
