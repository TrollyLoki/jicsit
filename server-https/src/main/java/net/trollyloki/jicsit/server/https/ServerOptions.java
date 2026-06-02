package net.trollyloki.jicsit.server.https;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
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
 *     <li>{@link #SEND_CRASH_REPORTS}
 *     <li>{@link #SEND_GAMEPLAY_DATA}
 *     <li>{@link #NETWORK_QUALITY}
 *     <li>{@link #ENABLE_SEASONAL_EVENTS}
 *     <li>{@link #WEATHER_PRESET}
 * </ul>
 *
 * @param current current server option values
 * @param pending pending server option values
 * @see HttpsApi#renameServer(String)
 * @see HttpsApi#setAdminPassword(String)
 * @see HttpsApi#setClientPassword(String)
 * @see HttpsApi#setAutoLoadSessionName(String)
 */
@NullMarked
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
     *
     * @deprecated Replaced with {@link #ENABLE_SEASONAL_EVENTS}
     */
    @Deprecated(since = "2.0.0")
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
     * If crash reports will be sent to Coffee Stain Studios when the server crashes.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String SEND_CRASH_REPORTS = "FG.AgreeToCrashUpload";

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

    /**
     * If all seasonal event content (such as FICSMAS and the Anniversary event) should remain in the game.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String ENABLE_SEASONAL_EVENTS = "FG.EnableSeasonalEvents";

    /**
     * The weather preset for the world. This affects the frequency and intensity of rain and other weather events.
     * <p>
     * Example values:
     * <ul>
     *     <li>{@code "0"} is Default
     *     <li>{@code "1"} is Dry
     *     <li>{@code "2"} is Wet
     *     <li>{@code "3"} is The Great MASSAGE-2 (AB)b
     *     <li>{@code "4"} is Clear
     *     <li>{@code "5"} is Raining Kittens and Puppies
     *     <li>{@code "6"} is Extreme
     * </ul>
     */
    public static final String WEATHER_PRESET = "FG.WeatherPreset";

    /**
     * Network quality options.
     *
     * @see #NETWORK_QUALITY
     * @see Builder#networkQuality(NetworkQuality)
     */
    public enum NetworkQuality {

        /**
         * Low.
         */
        LOW,

        /**
         * Medium.
         */
        MEDIUM,

        /**
         * High.
         */
        HIGH,

        /**
         * Ultra.
         */
        ULTRA,

    }

    /**
     * Weather preset options.
     *
     * @see #WEATHER_PRESET
     * @see Builder#weatherPreset(WeatherPreset)
     */
    public enum WeatherPreset {

        /**
         * Default weather.
         */
        DEFAULT,

        /**
         * Overcast, wind, and clear weather.
         */
        DRY,

        /**
         * Rain, overcast, and clear weather. Some thunder.
         */
        WET,

        /**
         * Wind and rain, with a touch of overcast and thunderstorms.
         */
        GREAT_MASSAGE,

        /**
         * Clear weather with a touch of wind, rain, and thunder.
         */
        CLEAR,

        /**
         * Chance of rain and windy weather. Low chance of thunder and clear weather.
         */
        RAINING_KITTENS_AND_PUPPIES,

        /**
         * Chance of rain, thunderstorms, and wind. Low chance of clear weather.
         */
        EXTREME,

    }

    /**
     * Creates a builder for server options.
     *
     * @return new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for server options.
     */
    public static final class Builder {

        private static final double NANOS_PER_SECOND = 1_000_000_000.0;
        private static final double NANOS_PER_MINUTE = 60 * NANOS_PER_SECOND;

        private final Map<String, String> map;

        private Builder() {
            this.map = new HashMap<>();
        }

        /**
         * Creates a map containing the options applied to this builder.
         *
         * @return options map
         * @see HttpsApi#applyServerOptions(Map)
         */
        public Map<String, String> build() {
            return Map.copyOf(map);
        }

        private Builder putBoolean(String key, boolean value) {
            map.put(key, value ? "True" : "False");
            return this;
        }

        private Builder putDouble(String key, double value) {
            map.put(key, Double.toString(value));
            return this;
        }

        private Builder putEnum(String key, Enum<?> constant) {
            map.put(key, Integer.toString(constant.ordinal()));
            return this;
        }

        /**
         * Changes if the server should be automatically paused when no players are connected.
         *
         * @param enabled {@code true} if it should pause, or {@code false} if it should not
         * @return this builder
         */
        public Builder autoPause(boolean enabled) {
            return putBoolean(AUTO_PAUSE, enabled);
        }

        /**
         * Changes if the server should automatically save the game when a player disconnects.
         *
         * @param enabled {@code true} if it should save, or {@code false} if it should not
         * @return this builder
         */
        public Builder autosaveOnDisconnect(boolean enabled) {
            return putBoolean(AUTO_SAVE_ON_DISCONNECT, enabled);
        }

        /**
         * Changes if all seasonal event content (such as FICSMAS) should be removed from the game.
         *
         * @param disabled {@code true} if events should be disabled, or {@code false} if they should remain enabled
         * @return this builder
         * @deprecated Replaced with {@link #enableSeasonalEvents(boolean)}
         */
        @Deprecated(since = "2.0.0")
        public Builder disableSeasonalEvents(boolean disabled) {
            enableSeasonalEvents(!disabled);
            return putBoolean(DISABLE_SEASONAL_EVENTS, disabled);
        }

        /**
         * Changes the amount of time between autosaves.
         *
         * @param interval duration between autosaves
         * @return this builder
         */
        public Builder autosaveInterval(Duration interval) {
            return putDouble(AUTOSAVE_INTERVAL, interval.getSeconds() + interval.getNano() / NANOS_PER_SECOND);
        }

        /**
         * Changes the time of day the server should restart at.
         *
         * @param time time of day
         * @return this builder
         */
        public Builder serverRestartSchedule(LocalTime time) {
            return putDouble(SERVER_RESTART_SCHEDULE, time.toNanoOfDay() / NANOS_PER_MINUTE);
        }

        /**
         * Changes if crash reports will be sent to Coffee Stain Studios when the server crashes.
         *
         * @param enabled {@code true} if data can be sent, or {@code false} if it cannot
         * @return this builder
         */
        public Builder sendCrashReports(boolean enabled) {
            return putBoolean(SEND_CRASH_REPORTS, enabled);
        }

        /**
         * Changes if data can be sent to Coffee Stain Studios while playing. Requires a restart to apply.
         *
         * @param enabled {@code true} if data can be sent, or {@code false} if it cannot
         * @return this builder
         */
        public Builder sendGameplayData(boolean enabled) {
            return putBoolean(SEND_GAMEPLAY_DATA, enabled);
        }

        /**
         * Changes network quality.
         *
         * @param quality {@link NetworkQuality}
         * @return this builder
         */
        public Builder networkQuality(NetworkQuality quality) {
            return putEnum(NETWORK_QUALITY, quality);
        }

        /**
         * Changes if all seasonal event content (such as FICSMAS and the Anniversary event) should remain in the game.
         *
         * @param enabled {@code true} if events should remain enabled, or {@code false} if they should be disabled
         * @return this builder
         */
        public Builder enableSeasonalEvents(boolean enabled) {
            return putBoolean(ENABLE_SEASONAL_EVENTS, enabled);
        }

        /**
         * Changes the weather preset for the world.
         * This affects the frequency and intensity of rain and other weather events.
         *
         * @param preset {@link WeatherPreset}
         * @return this builder
         */
        public Builder weatherPreset(WeatherPreset preset) {
            return putEnum(WEATHER_PRESET, preset);
        }

    }

}
