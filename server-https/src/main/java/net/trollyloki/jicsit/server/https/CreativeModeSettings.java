package net.trollyloki.jicsit.server.https;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

/**
 * The currently applied Creative Mode settings.
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
 * @param enabled  {@code true} if Creative Mode is enabled for the currently loaded session, or {@code false} if it is not
 * @param settings setting values
 */
@NullMarked
public record CreativeModeSettings(
        @JsonProperty("creativeModeEnabled") boolean enabled,
        @JsonProperty("advancedGameSettings") Map<String, String> settings
) {

    /**
     * Buildings will function without power.
     * <p>
     * Applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String NO_POWER = "FG.GameRules.NoPower";

    /**
     * Vehicles, drones, portals, and the power augmenter will function without any fuel.
     * <p>
     * Applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String NO_FUEL = "FG.GameRules.NoFuelCost";

    /**
     * Anything that needs to be unlocked can be unlocked without having to pay any resources or other costs.
     * <p>
     * Applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String NO_UNLOCK_COST = "FG.GameRules.NoUnlockCost";

    /**
     * Immediately unlocks alternate recipes when all their requirements are met.
     * <p>
     * Applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String UNLOCK_ALTERNATE_RECIPES_INSTANTLY = "FG.GameRules.UnlockInstantAltRecipes";

    /**
     * Prevents arachnid creatures from spawning.
     * <p>
     * Applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String DISABLE_ARACHNID_CREATURES = "FG.GameRules.DisableArachnidCreatures";

    /**
     * Unlocks all tiers in the game.
     * <p>
     * <strong>Irreversible</strong>, applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String UNLOCK_ALL_TIERS = "FG.GameRules.GiveAllTiers";

    /**
     * Unlocks all research in the MAM.
     * <p>
     * <strong>Irreversible</strong>, applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String UNLOCK_ALL_RESEARCH = "FG.GameRules.UnlockAllResearchSchematics";

    /**
     * Unlocks everything in the AWESOME Shop.
     * <p>
     * <strong>Irreversible</strong>, applies to the entire session and affects all players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String UNLOCK_ALL_IN_AWESOME_SHOP = "FG.GameRules.UnlockAllResourceSinkSchematics";

    /**
     * Sets the tier the game should start at.
     * <p>
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
     *     <li>{@code "10"} is Unlock All Tiers
     * </ul>
     */
    public static final String STARTING_TIER = "FG.GameRules.StartingTier";

    /**
     * Sets the Space Elevator phase.
     * <p>
     * <strong>Irreversible</strong>, applies to the entire session and affects all players.
     * <p>
     * Example values:
     * <ul>
     *     <li>{@code "0"} is Onboarding
     *     <li>{@code "1"} is Distribution Platform (Phase 1)
     *     <li>{@code "2"} is Construction Dock (Phase 2)
     *     <li>{@code "3"} is Main Body (Phase 3)
     *     <li>{@code "4"} is Propulsion Systems (Phase 4)
     *     <li>{@code "5"} is Assembly (Phase 5)
     *     <li>{@code "6"} is Launch
     *     <li>{@code "7"} is Completed
     * </ul>
     */
    public static final String SET_GAME_PHASE = "FG.GameRules.SetGamePhase";

    /**
     * Gives items to the player.
     */
    public static final String GIVE_ITEMS = "FG.GameRules.GiveItems";

    /**
     * Buildings, blueprints, and customizer items have no cost.
     * <p>
     * Only applies to new players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String NO_BUILD_COST = "FG.PlayerRules.NoBuildCost";

    /**
     * Makes the player invincible.
     * <p>
     * Only applies to new players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String GOD_MODE = "FG.PlayerRules.GodMode";

    /**
     * Enables flight.
     * <p>
     * Only applies to new players.
     * <p>
     * Example values: {@code "True"} or {@code "False"}
     */
    public static final String FLIGHT_MODE = "FG.PlayerRules.FlightMode";

    /**
     * Creates a builder for Creative Mode settings.
     *
     * @return new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for Creative Mode settings.
     */
    public static final class Builder {

        private final Map<String, String> map;

        private Builder() {
            this.map = new HashMap<>();
        }

        /**
         * Creates a map containing the settings applied to this builder.
         *
         * @return settings map
         * @see HttpsApi#applyCreativeModeSettings(Map)
         * @see NewGameData
         */
        public Map<String, String> build() {
            return Map.copyOf(map);
        }

        private Builder putTrue(String key) {
            map.put(key, "True");
            return this;
        }

        /**
         * Buildings will function without power.
         * <p>
         * Applies to the entire session and affects all players.
         *
         * @return this builder
         */
        public Builder noPower() {
            return putTrue(NO_POWER);
        }

        /**
         * Vehicles, drones, portals, and the power augmenter will function without any fuel.
         * <p>
         * Applies to the entire session and affects all players.
         *
         * @return this builder
         */
        public Builder noFuel() {
            return putTrue(NO_FUEL);
        }

        /**
         * Anything that needs to be unlocked can be unlocked without having to pay any resources or other costs.
         * <p>
         * Applies to the entire session and affects all players.
         *
         * @return this builder
         */
        public Builder noUnlockCost() {
            return putTrue(NO_UNLOCK_COST);
        }

        /**
         * Immediately unlocks alternate recipes when all their requirements are met.
         * <p>
         * Applies to the entire session and affects all players.
         *
         * @return this builder
         */
        public Builder unlockAlternateRecipesInstantly() {
            return putTrue(UNLOCK_ALTERNATE_RECIPES_INSTANTLY);
        }

        /**
         * Prevents arachnid creatures from spawning.
         * <p>
         * Applies to the entire session and affects all players.
         *
         * @return this builder
         */
        public Builder disableArachnidCreatures() {
            return putTrue(DISABLE_ARACHNID_CREATURES);
        }

        /**
         * Unlocks all tiers in the game.
         * <p>
         * <strong>Irreversible</strong>, applies to the entire session and affects all players.
         *
         * @return this builder
         */
        public Builder unlockAllTiers() {
            map.put(STARTING_TIER, "10");
            return putTrue(UNLOCK_ALL_TIERS);
        }

        /**
         * Unlocks all research in the MAM.
         * <p>
         * <strong>Irreversible</strong>, applies to the entire session and affects all players.
         *
         * @return this builder
         */
        public Builder unlockAllResearch() {
            return putTrue(UNLOCK_ALL_RESEARCH);
        }

        /**
         * Unlocks everything in the AWESOME Shop.
         * <p>
         * <strong>Irreversible</strong>, applies to the entire session and affects all players.
         *
         * @return this builder
         */
        public Builder unlockAllInAwesomeShop() {
            return putTrue(UNLOCK_ALL_IN_AWESOME_SHOP);
        }

        /**
         * Sets the tier the game should start at.
         * <p>
         * Applies to the entire session and affects all players.
         *
         * @param tier starting tier (Onboarding is Tier 0)
         * @return this builder
         */
        public Builder startingTier(int tier) {
            if (tier < 0) throw new IllegalArgumentException("Starting tier cannot be negative");
            map.put(STARTING_TIER, Integer.toString(tier));
            return this;
        }

        /**
         * Sets the Space Elevator phase.
         * <p>
         * <strong>Irreversible</strong>, applies to the entire session and affects all players.
         *
         * @param phase game phase (Launch is Phase 6 and Completed is Phase 7)
         * @return this builder
         */
        public Builder setGamePhase(int phase) {
            if (phase < 0) throw new IllegalArgumentException("Game phase cannot be negative");
            map.put(SET_GAME_PHASE, Integer.toString(phase));
            return this;
        }

        /**
         * Buildings, blueprints, and customizer items have no cost.
         * <p>
         * Only applies to new players.
         *
         * @return this builder
         */
        public Builder noBuildCost() {
            return putTrue(NO_BUILD_COST);
        }

        /**
         * Makes the player invincible.
         * <p>
         * Only applies to new players.
         *
         * @return this builder
         */
        public Builder godMode() {
            return putTrue(GOD_MODE);
        }

        /**
         * Enables flight.
         * <p>
         * Only applies to new players.
         *
         * @return this builder
         */
        public Builder flightMode() {
            return putTrue(FLIGHT_MODE);
        }

    }

}
