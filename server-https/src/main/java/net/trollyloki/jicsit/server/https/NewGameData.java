package net.trollyloki.jicsit.server.https;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Parameters for a new session.
 * <p>
 * <strong>Note:</strong> Onboarding is always skipped on vanilla servers.
 * <p>
 * Vanilla servers support the following starting locations:
 * <ul>
 *     <li>{@link #GRASS_FIELDS}
 *     <li>{@link #ROCKY_DESERT}
 *     <li>{@link #NORTHERN_FOREST}
 *     <li>{@link #DUNE_DESERT}
 * </ul>
 *
 * @param sessionName          name of the session
 * @param mapName              path name to the map package, default level if not specified
 * @param startingLocation     name of the starting location to use
 * @param skipOnboarding       {@code true} if onboarding should be skipped, or {@code false} if it shouldn't be skipped
 * @param creativeModeSettings {@link CreativeModeSettings} to apply
 * @param gameModeSettings     {@link GameModeSettings} to apply
 * @param customOptions        custom options to pass to the session URL, not used by vanilla servers
 */
@NullMarked
public record NewGameData(
        String sessionName,
        @Nullable String mapName,
        String startingLocation,
        @JsonProperty("bSkipOnboarding") boolean skipOnboarding,
        @JsonProperty("advancedGameSettings") Map<String, String> creativeModeSettings,
        Map<String, String> gameModeSettings,
        @JsonProperty("customOptionsOnlyForModding") Map<String, String> customOptions
) {

    /**
     * The value for the "Grass Fields" starting location.
     */
    public static final String GRASS_FIELDS = "Grass Fields";

    /**
     * The value for the "Rocky Desert" starting location.
     */
    public static final String ROCKY_DESERT = "Rocky Desert";

    /**
     * The value for the "Northern Forest" starting location.
     */
    public static final String NORTHERN_FOREST = "Northern Forest";

    /**
     * The value for the "Dune Desert" starting location.
     */
    public static final String DUNE_DESERT = "DuneDesert"; // yes this one doesn't have a space, ask the devs

    /**
     * Creates a builder for new game data.
     *
     * @param sessionName name of the session
     * @return new builder
     */
    public static Builder builder(String sessionName) {
        return new Builder(sessionName);
    }

    /**
     * A builder for new game data.
     */
    public static final class Builder {

        private final String sessionName;
        private @Nullable String mapName = null;
        private String startingLocation;
        private boolean skipOnboarding = false;
        private Map<String, String> creativeModeSettings = Collections.emptyMap();
        private Map<String, String> gameModeSettings = Collections.emptyMap();
        private Map<String, String> customOptions = Collections.emptyMap();

        private Builder(String sessionName) {
            this.sessionName = sessionName;
            // Choose a random starting location by default
            this.startingLocation = switch (ThreadLocalRandom.current().nextInt(4)) {
                case 0 -> GRASS_FIELDS;
                case 1 -> ROCKY_DESERT;
                case 2 -> NORTHERN_FOREST;
                default -> DUNE_DESERT;
            };
        }

        /**
         * Creates a {@link NewGameData} record containing the settings applied to this builder.
         *
         * @return new game data
         * @see HttpsApi#createNewSession(NewGameData)
         */
        public NewGameData build() {
            return new NewGameData(
                    sessionName,
                    mapName,
                    startingLocation,
                    skipOnboarding,
                    creativeModeSettings,
                    gameModeSettings,
                    customOptions
            );
        }

        /**
         * Sets the custom map name for the new session.
         *
         * @param mapName path name to the map package
         * @return this builder
         */
        public Builder mapName(String mapName) {
            this.mapName = mapName;
            return this;
        }

        /**
         * Sets the starting location for the new session.
         * <p>
         * Vanilla servers support the following starting locations:
         * <ul>
         *     <li>{@link #GRASS_FIELDS}
         *     <li>{@link #ROCKY_DESERT}
         *     <li>{@link #NORTHERN_FOREST}
         *     <li>{@link #DUNE_DESERT}
         * </ul>
         *
         * @param startingLocation name of the starting location to use, or empty for a random starting location
         * @return this builder
         */
        public Builder startingLocation(String startingLocation) {
            this.startingLocation = startingLocation;
            return this;
        }

        /**
         * Skips onboarding for the new session.
         * <p>
         * <strong>Note:</strong> Onboarding is always skipped on vanilla servers.
         *
         * @return this builder
         */
        public Builder skipOnboarding() {
            this.skipOnboarding = true;
            return this;
        }

        /**
         * Applies Creative Mode settings to the new session.
         *
         * @param creativeModeSettings {@link CreativeModeSettings} to apply
         * @return this builder
         * @see CreativeModeSettings#builder()
         */
        public Builder creativeModeSettings(Map<String, String> creativeModeSettings) {
            this.creativeModeSettings = Map.copyOf(creativeModeSettings);
            return this;
        }

        /**
         * Applies Game Mode settings to the new session.
         *
         * @param gameModeSettings {@link GameModeSettings Game Mode settings} to apply
         * @return this builder
         * @see GameModeSettings#builder()
         */
        public Builder gameModeSettings(Map<String, String> gameModeSettings) {
            this.gameModeSettings = Map.copyOf(gameModeSettings);
            return this;
        }

        /**
         * Sets custom mod options for the new session. These are not used by vanilla servers.
         *
         * @param customOptions custom options to pass to the session URL
         * @return this builder
         */
        public Builder customOptions(Map<String, String> customOptions) {
            this.customOptions = Map.copyOf(customOptions);
            return this;
        }

    }

}
