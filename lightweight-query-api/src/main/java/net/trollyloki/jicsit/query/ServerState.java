package net.trollyloki.jicsit.query;

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

}
