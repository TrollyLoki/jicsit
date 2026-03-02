package net.trollyloki.jicsit.server.api.https;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

/**
 * The state of the server.
 *
 * @param activeSessionName        name of the currently loaded session
 * @param connectedPlayerCount     number of players currently connected
 * @param playerLimit              maximum number of players that can be connected
 * @param techTier                 maximum tech tier of all unlocked milestones
 * @param activeSchematic          currently selected active milestone
 * @param gamePhase                current game phase
 * @param isGameRunning            {@code true} if a save is loaded, or {@code false} if the server is waiting for a session to be created
 * @param totalGameDuration        amount of time that the current session has been running for in total
 * @param isGamePaused             {@code true} if the game is paused, or {@code false} otherwise
 * @param averageTickRate          average server tick rate in ticks per second
 * @param autoLoadSessionName      name of the session that will be loaded automatically when the server starts
 */
@NullMarked
public record ServerGameState(
        String activeSessionName,
        @JsonProperty("numConnectedPlayers") int connectedPlayerCount,
        int playerLimit,
        int techTier,
        String activeSchematic,
        String gamePhase,
        boolean isGameRunning,
        Duration totalGameDuration,
        boolean isGamePaused,
        double averageTickRate,
        String autoLoadSessionName
) {

    //TODO: Constants for active schematic and game phase values?

}
