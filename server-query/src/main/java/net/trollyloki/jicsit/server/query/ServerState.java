package net.trollyloki.jicsit.server.query;

import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * The detailed state of a server.
 *
 * @param status    current {@link ServerStatus status/state} that the server is in
 * @param build     build number that the server is running
 * @param flags     flags describing the server, primarily for modding
 * @param subStates list of {@link ServerSubState substates}
 * @param name      server name
 */
@NullMarked
public record ServerState(ServerStatus status, int build, long flags, List<ServerSubState> subStates, String name) {

    /**
     * Checks a server flag.
     *
     * @param index bit index
     * @return {@code true} if the flag is set, or {@code false} if it is not
     */
    public boolean getFlag(int index) {
        return (flags & (1L << index)) != 0;
    }

    /**
     * Checks if the server is considered modded.
     * <p>
     * Vanilla clients will not try to connect to modded servers.
     *
     * @return {@code true} if the server is modded, or {@code false} if it is vanilla
     */
    public boolean isModded() {
        return getFlag(0);
    }

    /**
     * Gets the version of a specific substate.
     *
     * @param id ID of the substate being changed
     * @return current changelist of the substate, or {@code 0} if the substate is not listed
     * @see ServerSubState
     */
    public short subStateVersion(byte id) {
        for (ServerSubState subState : subStates) {
            if (subState.id() == id) {
                return subState.version();
            }
        }
        return 0;
    }

}
