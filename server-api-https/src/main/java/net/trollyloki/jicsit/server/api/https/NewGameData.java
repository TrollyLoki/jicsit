package net.trollyloki.jicsit.server.api.https;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

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
 * @param advancedGameSettings {@link AdvancedGameSettings Advanced Game Settings} values to apply to the session
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
     * @param advancedGameSettings {@link AdvancedGameSettings Advanced Game Settings} values to apply to the session
     * @param customOptions        custom options to pass to the session URL, not used by vanilla servers
     */
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
     * @param advancedGameSettings {@link AdvancedGameSettings Advanced Game Settings} values to apply to the session
     */
    public NewGameData(String sessionName, String startingLocation, Map<String, String> advancedGameSettings) {
        this(sessionName, startingLocation, true, advancedGameSettings, Map.of());
    }

    /**
     * Parameters for a new session at a random starting location on the default map.
     *
     * @param sessionName          name of the session
     * @param advancedGameSettings {@link AdvancedGameSettings Advanced Game Settings} values to apply to the session
     */
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
     */
    public NewGameData(String sessionName, String startingLocation) {
        this(sessionName, startingLocation, Map.of());
    }

    /**
     * Parameters for a new session at a random starting location on the default map.
     *
     * @param sessionName name of the session
     */
    public NewGameData(String sessionName) {
        this(sessionName, "");
    }

}
