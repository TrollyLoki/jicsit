package net.trollyloki.jicsit.modding;

import org.jspecify.annotations.NullMarked;

import java.util.Optional;

/**
 * A list of {@link ModVersion ModVersions}.
 */
@NullMarked
public interface ModVersionList extends Iterable<ModVersion> {

    /**
     * Finds the latest (non-pre-release) mod version that supports a given build of the game.
     *
     * @param build build version
     * @return latest matching {@link ModVersion}, or an empty optional if none exists
     */
    Optional<ModVersion> findLatestSupportingBuild(int build);

}
