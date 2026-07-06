package net.trollyloki.jicsit.modding;

import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * A specific version of a mod.
 *
 * @param id SMR version ID
 * @param version semantic version
 * @param gameVersion game version requirement
 * @param targets list of {@link ModVersionTarget ModVersionTargets}
 */
@NullMarked
public record ModVersion(
        String id,
        String version,
        String gameVersion,
        List<ModVersionTarget> targets
) implements Versioned {

    /**
     * Checks if this mod version supports a given build of the game.
     *
     * @param build build version
     * @return {@code true} if supported, or {@code false} if not supported
     */
    public boolean supportsBuild(int build) {
        if (gameVersion.startsWith(">=")) {
            int minBuild = Integer.parseInt(gameVersion.substring(2));
            return build >= minBuild;
        } else {
            throw new UnsupportedOperationException("Unknown game version specifier: " + gameVersion);
        }
    }

    /**
     * Checks if this mod version supports a specific installation target.
     *
     * @param targetName installation target
     * @return {@code true} if supported, or {@code false} if not supported
     */
    public boolean supportsTarget(String targetName) {
        for (ModVersionTarget target : targets) {
            if (target.targetName().equals(targetName)) {
                return true;
            }
        }
        return false;
    }

}
