package net.trollyloki.jicsit.https;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * The currently applied Advanced Game Settings.
 * <p>
 * Vanilla servers support the following settings:
 * <ul>
 *     <li>{@link #NO_POWER}
 *     <li>{@link #NO_FUEL}
 *     <li>{@link #NO_UNLOCK_COST}
 *     <li>{@link #UNLOCK_ALTERNATE_RECIPES_INSTANTLY}
 *     <li>{@link #DISABLE_ARACHNID_CREATURES}
 *     <li>{@link #UNLOCK_ALL_TIERS}
 *     <li>{@link #UNLOCK_ALL_RESEARCH}
 *     <li>{@link #UNLOCK_ALL_IN_AWESOME_SHOP}
 *     <li>{@link #STARTING_TIER}
 *     <li>{@link #SET_GAME_PHASE}
 *     <li>{@link #GIVE_ITEMS}
 *     <li>{@link #NO_BUILD_COST}
 *     <li>{@link #GOD_MODE}
 *     <li>{@link #FLIGHT_MODE}
 * </ul>
 *
 * @param enabled  {@code true} if Advanced Game Settings are enabled for the currently loaded session, or {@code false} if they are not
 * @param settings Advanced Game Settings values
 */
public record AdvancedGameSettings(
        @JsonProperty("creativeModeEnabled") boolean enabled,
        @JsonProperty("advancedGameSettings") Map<String, String> settings
) {

    /**
     * Buildings will function without power.
     * Applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String NO_POWER = "FG.GameRules.NoPower";

    /**
     * Vehicles, drones, portals, and the power augmenter will function without any fuel.
     * Applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String NO_FUEL = "FG.GameRules.NoFuelCost";

    /**
     * Anything that needs to be unlocked can be unlocked without having to pay any resources or other costs.
     * Applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String NO_UNLOCK_COST = "FG.GameRules.NoUnlockCost";

    /**
     * Immediately unlocks alternate recipes when all their requirements are met.
     * Applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String UNLOCK_ALTERNATE_RECIPES_INSTANTLY = "FG.GameRules.UnlockInstantAltRecipes";

    /**
     * Prevents arachnid creatures from spawning.
     * Applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String DISABLE_ARACHNID_CREATURES = "FG.GameRules.DisableArachnidCreatures";

    /**
     * Unlocks all tiers in the game.
     * <strong>Irreversible</strong>, applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String UNLOCK_ALL_TIERS = "FG.GameRules.GiveAllTiers";

    /**
     * Unlocks all research in the MAM.
     * <strong>Irreversible</strong>, applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String UNLOCK_ALL_RESEARCH = "FG.GameRules.UnlockAllResearchSchematics";

    /**
     * Unlocks everything in the AWESOME Shop.
     * <strong>Irreversible</strong>, applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String UNLOCK_ALL_IN_AWESOME_SHOP = "FG.GameRules.UnlockAllResourceSinkSchematics";

    /**
     * Selects the tier the game should start at, and adjusts the Space Elevator phase accordingly.
     * Applies to the entire session and affects all players.
     * <p>
     * Example values:
     * <ul>
     *     <li>{@code "0"} is Onboarding
     *     <li>{@code "1"} is Tier 1
     *     <li>{@code "2"} is Tier 2
     *     <li>{@code "3"} is Tier 3
     *     <li>{@code "4"} is Tier 4
     *     <li>{@code "5"} is Tier 5
     *     <li>{@code "6"} is Tier 6
     *     <li>{@code "7"} is Tier 7
     *     <li>{@code "8"} is Tier 8
     *     <li>{@code "9"} is Tier 9
     * </ul>
     */
    public static final String STARTING_TIER = "FG.GameRules.StartingTier";

    /**
     * Selects the Space Elevator phase, and adjusts the current tier accordingly.
     * <strong>Irreversible</strong>, applies to the entire session and affects all players.
     * <p>
     * Example values:
     * <ul>
     *     <li>{@code "0"} is Onboarding
     *     <li>{@code "1"} is Phase 1 (Distribution Platform)
     *     <li>{@code "2"} is Phase 2 (Construction Dock)
     *     <li>{@code "3"} is Phase 3 (Main Body)
     *     <li>{@code "4"} is Phase 4 (Propulsion Systems)
     *     <li>{@code "5"} is Phase 5 (Assembly)
     * </ul>
     */
    public static final String SET_GAME_PHASE = "FG.GameRules.SetGamePhase";

    /**
     * Gives items to the player.
     */
    public static final String GIVE_ITEMS = "FG.GameRules.GiveItems";

    /**
     * Buildings, blueprints, and customizer items have no cost.
     * This setting is per-player.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String NO_BUILD_COST = "FG.PlayerRules.NoBuildCost";

    /**
     * Makes the player invincible.
     * This setting is per-player.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String GOD_MODE = "FG.PlayerRules.GodMode";

    /**
     * Enables flight.
     * This setting is per-player.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String FLIGHT_MODE = "FG.PlayerRules.FlightMode";

}
