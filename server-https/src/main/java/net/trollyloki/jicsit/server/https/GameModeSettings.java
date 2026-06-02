package net.trollyloki.jicsit.server.https;

import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

/**
 * Vanilla servers support the following settings:
 * <ul>
 *     <li>{@link #SPACE_ELEVATOR_COST_MULTIPLIER}
 *     <li>{@link #RECIPE_COST_MULTIPLIER}
 *     <li>{@link #POWER_CONSUMPTION_MULTIPLIER}
 *     <li>{@link #RESOURCE_NODE_RANDOMIZATION}
 *     <li>{@link #RESOURCE_NODE_PURITY}
 *     <li>{@link #SEED}
 * </ul>
 */
@NullMarked
public final class GameModeSettings {
    private GameModeSettings() {
    }

    /**
     * Changes the part cost for each Space Elevator phase by a set multiplier.
     * The value is 100 times the multiplier.
     * <p>
     * Applies to the entire session and affects all players.
     * <p>
     * Example values:
     * <ul>
     *     <li>{@code "100"} leaves the cost unchanged
     *     <li>{@code "25"} divides the cost by 4
     *     <li>{@code "50"} halves the cost
     *     <li>{@code "200"} doubles the cost
     *     <li>{@code "10000"} multiplies the cost by 100
     *     <li>{@code "0"} sets the cost to one of each required part
     * </ul>
     */
    public static final String SPACE_ELEVATOR_COST_MULTIPLIER = "FG.GameMode.SpacePartsCostMultiplier";

    /**
     * Changes the part cost for each recipe by a set multiplier.
     * The value is 100 times the multiplier.
     * <p>
     * Applies to the entire session and affects all players.
     * <p>
     * Example values:
     * <ul>
     *     <li>{@code "100"} leaves the cost unchanged
     *     <li>{@code "25"} divides the cost by 4
     *     <li>{@code "50"} halves the cost
     *     <li>{@code "200"} doubles the cost
     *     <li>{@code "0"} sets the cost to one of each required part and no fluid is consumed
     * </ul>
     */
    public static final String RECIPE_COST_MULTIPLIER = "FG.GameMode.PartsCostMultiplier";

    /**
     * Changes the power consumption of everything in the game by a set multiplier.
     * The value is 100 times the multiplier.
     * <p>
     * Applies to the entire session and affects all players.
     * <p>
     * Example values:
     * <ul>
     *     <li>{@code "100"} leaves power consumption unchanged
     *     <li>{@code "25"} divides power consumption by 4
     *     <li>{@code "50"} halves power consumption
     *     <li>{@code "200"} doubles power consumption
     *     <li>{@code "500"} multiplies power consumption by 5
     *     <li>{@code "0"} sets the power consumption of all machines to 0.1 MW
     * </ul>
     */
    public static final String POWER_CONSUMPTION_MULTIPLIER = "FG.GameMode.EnergyCostMultiplier";

    /**
     * Randomizes resource node types.
     * <p>
     * Applies to the entire session and affects all players.
     * <p>
     * Example values:
     * <ul>
     *     <li>{@code "0"} is Default (base game experience)
     *     <li>{@code "1"} is Random
     *     <li>{@code "2"} is Basic Resource Rich (more iron, coal, copper, caterium, and limestone)
     *     <li>{@code "3"} is Advanced Resource Rich (more quartz, sulfur, bauxite, SAM, and uranium)
     *     <li>{@code "4"} is Fossil Fuel Rich (more oil, coal, and sulfur)
     * </ul>
     */
    public static final String RESOURCE_NODE_RANDOMIZATION = "FG.GameMode.NodeRandomization";

    /**
     * Changes resource node purities.
     * Values containing "Mostly" affect nodes by decreasing or increasing their purity by one level.
     * <p>
     * Applies to the entire session and affects all players.
     * <p>
     * Example values:
     * <ul>
     *     <li>{@code "0"} is Default (base game experience)
     *     <li>{@code "1"} is All Pure
     *     <li>{@code "2"} is Average (All Normal)
     *     <li>{@code "3"} is All Impure
     *     <li>{@code "4"} is Random
     *     <li>{@code "5"} is Mostly Pure
     *     <li>{@code "6"} is Mostly Impure
     * </ul>
     */
    public static final String RESOURCE_NODE_PURITY = "FG.GameMode.NodePuritySettings";

    /**
     * Seed for the session.
     * The value is an integer and can be shared to allow other players to create a world with the same settings.
     * <p>
     * Applies to the entire session and affects all players.
     * <p>
     * Example values:
     * <ul>
     *     <li>{@code "0"} generates a random seed
     *     <li>{@code "12345"}
     *     <li>{@code "-1"}
     *     <li>{@code "-2147483648"}
     *     <li>{@code "2147483647"}
     * </ul>
     */
    public static final String SEED = "FG.GameMode.NodeRandomizationSeed";

    /**
     * Resource node randomization options.
     *
     * @see #RESOURCE_NODE_RANDOMIZATION
     * @see Builder#resourceNodeRandomization(ResourceNodeRandomization)
     */
    public enum ResourceNodeRandomization {

        /**
         * Base game experience.
         */
        DEFAULT,

        /**
         * Completely random.
         */
        RANDOM,

        /**
         * More iron, coal, copper, caterium, and limestone.
         */
        BASIC_RESOURCE_RICH,

        /**
         * More quartz, sulfur, bauxite, SAM, and uranium.
         */
        ADVANCED_RESOURCE_RICH,

        /**
         * More oil, coal, and sulfur.
         */
        FOSSIL_FUEL_RICH,

    }

    /**
     * Resource node purity options.
     *
     * @see #RESOURCE_NODE_PURITY
     * @see Builder#resourceNodePurity(ResourceNodePurity)
     */
    public enum ResourceNodePurity {

        /**
         * Base game experience.
         */
        DEFAULT,

        /**
         * All pure nodes.
         */
        ALL_PURE,

        /**
         * All normal nodes.
         */
        AVERAGE,

        /**
         * All impure nodes.
         */
        ALL_IMPURE,

        /**
         * Completely random purities.
         */
        RANDOM,

        /**
         * Mostly pure nodes, with some normal nodes.
         */
        MOSTLY_PURE,

        /**
         * Mostly impure nodes, with some normal nodes.
         */
        MOSTLY_IMPURE,

    }

    /**
     * Creates a builder for Game Mode settings.
     *
     * @return new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for Game Mode settings.
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
         * @see NewGameData
         */
        public Map<String, String> build() {
            return Map.copyOf(map);
        }

        private Builder putInt(String key, int value) {
            map.put(key, Integer.toString(value));
            return this;
        }

        private Builder putMultiplier(String key, double multiplier) {
            return putInt(key, (int) (100 * multiplier));
        }

        private Builder putEnum(String key, Enum<?> constant) {
            return putInt(key, constant.ordinal());
        }

        /**
         * Changes the part cost for each Space Elevator phase by a set multiplier.
         * <p>
         * Applies to the entire session and affects all players.
         *
         * @param multiplier multiplier (truncated to two decimal places)
         * @return this builder
         */
        public Builder spaceElevatorCostMultiplier(double multiplier) {
            return putMultiplier(SPACE_ELEVATOR_COST_MULTIPLIER, multiplier);
        }

        /**
         * Changes the part cost for each recipe by a set multiplier.
         * <p>
         * Applies to the entire session and affects all players.
         *
         * @param multiplier multiplier (truncated to two decimal places)
         * @return this builder
         */
        public Builder recipeCostMultiplier(double multiplier) {
            return putMultiplier(RECIPE_COST_MULTIPLIER, multiplier);
        }

        /**
         * Changes the power consumption of everything in the game by a set multiplier.
         * <p>
         * Applies to the entire session and affects all players.
         *
         * @param multiplier multiplier (truncated to two decimal places)
         * @return this builder
         */
        public Builder powerConsumptionMultiplier(double multiplier) {
            return putMultiplier(POWER_CONSUMPTION_MULTIPLIER, multiplier);
        }

        /**
         * Randomizes resource node types.
         * <p>
         * Applies to the entire session and affects all players.
         *
         * @param randomization {@link ResourceNodeRandomization}
         * @return this builder
         */
        public Builder resourceNodeRandomization(ResourceNodeRandomization randomization) {
            return putEnum(RESOURCE_NODE_RANDOMIZATION, randomization);
        }

        /**
         * Changes resource node purities.
         * <p>
         * Applies to the entire session and affects all players.
         *
         * @param purity {@link ResourceNodePurity}
         * @return this builder
         */
        public Builder resourceNodePurity(ResourceNodePurity purity) {
            return putEnum(RESOURCE_NODE_PURITY, purity);
        }

        /**
         * Seed for the session.
         * The seed can be shared to allow other players to create a world with the same settings.
         * <p>
         * Applies to the entire session and affects all players.
         *
         * @param seed seed
         * @return this builder
         */
        public Builder seed(int seed) {
            return putInt(SEED, seed);
        }

    }

}
