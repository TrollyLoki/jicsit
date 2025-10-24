package net.trollyloki.jicsit.https;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * The currently applied server options and pending server options that require a session or server restart to be applied.
 * <p>
 * Vanilla servers support the following options:
 * <ul>
 *     <li>{@link #AUTO_PAUSE}
 *     <li>{@link #AUTO_SAVE_ON_DISCONNECT}
 *     <li>{@link #DISABLE_SEASONAL_EVENTS}
 *     <li>{@link #AUTOSAVE_INTERVAL}
 *     <li>{@link #SERVER_RESTART_SCHEDULE}
 *     <li>{@link #SEND_GAMEPLAY_DATA}
 *     <li>{@link #NETWORK_QUALITY}
 * </ul>
 *
 * @param current current server option values
 * @param pending pending server option values
 * @see HttpsApiClient#renameServer(String)
 * @see HttpsApiClient#setAdminPassword(String)
 * @see HttpsApiClient#setClientPassword(String)
 * @see HttpsApiClient#setAutoLoadSessionName(String)
 */
public record ServerOptions(
        @JsonProperty("serverOptions") Map<String, String> current,
        @JsonProperty("pendingServerOptions") Map<String, String> pending
) {

    /**
     * If the server should be automatically paused when no players are connected.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String AUTO_PAUSE = "FG.DSAutoPause";

    /**
     * If the server should automatically save the game when a player disconnects.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String AUTO_SAVE_ON_DISCONNECT = "FG.DSAutoSaveOnDisconnect";

    /**
     * If all seasonal event content (such as FICSMAS) should be removed from the game.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String DISABLE_SEASONAL_EVENTS = "FG.DisableSeasonalEvents";

    /**
     * The amount of time between autosaves in seconds.
     * <p>
     * Example values:
     * <ul>
     *     <li>{@code "0.0"} disables autosaves
     *     <li>{@code "300.0"} is 5 minutes
     *     <li>{@code "7200.0"} is 2 hours
     * </ul>
     */
    public static final String AUTOSAVE_INTERVAL = "FG.AutosaveInterval";

    /**
     * The time of day the server should restart at. The value is the number of minutes after midnight.
     * <p>
     * Example values:
     * <ul>
     *     <li>{@code "0.0"} is 00:00
     *     <li>{@code "1440.0"} is 24:00
     *     <li>{@code "720.0"} is 12:00
     *     <li>{@code "240.0"} is 04:00
     * </ul>
     */
    public static final String SERVER_RESTART_SCHEDULE = "FG.ServerRestartTimeSlot";

    /**
     * If data can be sent to Coffee Stain Studios while playing. Changing the value requires a restart.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String SEND_GAMEPLAY_DATA = "FG.SendGameplayData";

    /**
     * Increasing network quality may improve client load times and network performance at the cost of server framerate.
     * Experiment with this option to see what works for you.
     * <p>
     * Example values:
     * <ul>
     *     <li>{@code "0"} is Low
     *     <li>{@code "1"} is Medium
     *     <li>{@code "2"} is High
     *     <li>{@code "3"} is Ultra
     * </ul>
     */
    public static final String NETWORK_QUALITY = "FG.NetworkQuality";

}
