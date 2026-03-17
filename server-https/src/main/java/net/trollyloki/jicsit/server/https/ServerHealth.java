package net.trollyloki.jicsit.server.https;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NullMarked;

/**
 * A response to a health check.
 *
 * @param health     "healthy" if tick rate is above ten ticks per second, or "slow" otherwise
 * @param customData custom data from the server (vanilla servers provide an empty string)
 */
@NullMarked
public record ServerHealth(String health, @JsonProperty("serverCustomData") String customData) {

    /**
     * Checks if the server is healthy (running above ten ticks per second).
     *
     * @return {@code true} if the server is healthy, or {@code false} otherwise
     */
    public boolean isHealthy() {
        return health.equals("healthy");
    }

    /**
     * Checks if the server is slow (running below ten ticks per second).
     *
     * @return {@code true} if the server is slow, or {@code false} otherwise
     */
    public boolean isSlow() {
        return health.equals("slow");
    }

}
