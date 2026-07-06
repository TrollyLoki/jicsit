package net.trollyloki.jicsit.modding;

/**
 * A mod with a semantic version.
 */
public interface Versioned extends Comparable<Versioned> {

    /**
     * Gets the semantic version.
     *
     * @return semantic version.
     */
    String version();

    /**
     * Checks if this is a pre-release version.
     *
     * @return {@code true} if this is a pre-release, or {@code false} if this is a normal release
     */
    default boolean isPreRelease() {
        return SemVer.isPreRelease(version());
    }

    @Override
    default int compareTo(Versioned o) {
        return SemVer.compare(this.version(), o.version());
    }

}
