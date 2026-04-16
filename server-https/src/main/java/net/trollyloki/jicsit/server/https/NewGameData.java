package net.trollyloki.jicsit.server.https;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

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
 * @param startingLocation     name of the starting location to use, or empty for a random starting location
 * @param skipOnboarding       {@code true} if onboarding should be skipped, or {@code false} if it shouldn't be skipped
 * @param advancedGameSettings {@link AdvancedGameSettings Advanced Game Settings} to apply
 * @param customOptions        custom options to pass to the session URL, not used by vanilla servers
 */
@NullMarked
public record NewGameData(
        String sessionName,
        @Nullable String mapName,
        String startingLocation,
        @JsonProperty("bSkipOnboarding") boolean skipOnboarding,
        Map<String, String> advancedGameSettings,
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
     * Parameters for a new session on the default map.
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
     * @param startingLocation     name of the starting location to use, or empty for a random starting location
     * @param skipOnboarding       {@code true} if onboarding should be skipped, or {@code false} if it shouldn't be skipped
     * @param advancedGameSettings {@link AdvancedGameSettings Advanced Game Settings} to apply
     * @param customOptions        custom options to pass to the session URL, not used by vanilla servers
     * @deprecated {@link NewGameData} is complex and should be created using the {@link #builder(String) builder}
     * @see Builder#startingLocation(String)
     * @see Builder#skipOnboarding()
     * @see Builder#advancedGameSettings(Map)
     * @see Builder#customOptions(Map)
     */
    @Deprecated(forRemoval = true)
    public NewGameData(String sessionName, String startingLocation, boolean skipOnboarding, Map<String, String> advancedGameSettings, Map<String, String> customOptions) {
        this(sessionName, null, startingLocation, skipOnboarding, advancedGameSettings, customOptions);
    }

    /**
     * Parameters for a new session on the default map.
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
     * @param startingLocation     name of the starting location to use, or empty for a random starting location
     * @param advancedGameSettings {@link AdvancedGameSettings Advanced Game Settings} to apply
     * @deprecated {@link NewGameData} is complex and should be created using the {@link #builder(String) builder}
     * @see Builder#startingLocation(String)
     * @see Builder#advancedGameSettings(Map)
     */
    @Deprecated(forRemoval = true)
    public NewGameData(String sessionName, String startingLocation, Map<String, String> advancedGameSettings) {
        this(sessionName, startingLocation, true, advancedGameSettings, Map.of());
    }

    /**
     * Parameters for a new session at a random starting location on the default map.
     *
     * @param sessionName          name of the session
     * @param advancedGameSettings {@link AdvancedGameSettings Advanced Game Settings} to apply
     * @deprecated {@link NewGameData} is complex and should be created using the {@link #builder(String) builder}
     * @see Builder#advancedGameSettings(Map)
     */
    @Deprecated(forRemoval = true)
    public NewGameData(String sessionName, Map<String, String> advancedGameSettings) {
        this(sessionName, "", advancedGameSettings);
    }

    /**
     * Parameters for a new session on the default map.
     * <p>
     * Vanilla servers support the following starting locations:
     * <ul>
     *     <li>{@link #GRASS_FIELDS}
     *     <li>{@link #ROCKY_DESERT}
     *     <li>{@link #NORTHERN_FOREST}
     *     <li>{@link #DUNE_DESERT}
     * </ul>
     *
     * @param sessionName      name of the session
     * @param startingLocation name of the starting location to use, or empty for a random starting location
     * @deprecated {@link NewGameData} is complex and should be created using the {@link #builder(String) builder}
     * @see Builder#startingLocation(String)
     */
    @Deprecated(forRemoval = true)
    public NewGameData(String sessionName, String startingLocation) {
        this(sessionName, startingLocation, Map.of());
    }

    /**
     * Parameters for a new session at a random starting location on the default map.
     *
     * @param sessionName name of the session
     * @deprecated {@link NewGameData} is complex and should be created using the {@link #builder(String) builder}
     */
    @Deprecated(forRemoval = true)
    public NewGameData(String sessionName) {
        this(sessionName, "");
    }

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
        private Map<String, String> advancedGameSettings = Collections.emptyMap();
        private Map<String, String> customOptions = Collections.emptyMap();

        private Builder(String sessionName) {
            this.sessionName = sessionName;
            this.startingLocation = ""; // default to random starting location
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
                    advancedGameSettings,
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
         * Applies Advanced Game Settings to the new session.
         *
         * @param advancedGameSettings {@link AdvancedGameSettings Advanced Game Settings} to apply
         * @return this builder
         * @see AdvancedGameSettings#builder()
         */
        public Builder advancedGameSettings(Map<String, String> advancedGameSettings) {
            this.advancedGameSettings = Map.copyOf(advancedGameSettings);
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
