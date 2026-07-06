package net.trollyloki.jicsit.modding;

import org.jspecify.annotations.NullMarked;

/**
 * Parsing utilities for semantic versions.
 */
@NullMarked
public final class SemVer {

    /**
     * Checks if a semantic version represents a pre-release.
     *
     * @param semver semantic version
     * @return {@code true} if the version is a pre-release, or {@code false} if it is a normal release
     */
    public static boolean isPreRelease(String semver) {
        return semver.indexOf('-') != -1;
    }

    private static int[] parse(String semver) {
        int hyphenIndex = semver.indexOf('-');
        if (hyphenIndex >= 0) {
            // ignore pre-release versions for our purposes
            semver = semver.substring(0, hyphenIndex);
        }
        String[] split = semver.split("\\.");

        int[] versions = new int[3];
        for (int i = 0; i < split.length && i < versions.length; i++) {
            versions[i] = Integer.parseInt(split[i]);
        }
        return versions;
    }

    /**
     * Compares two semantic versions.
     * <p>
     * <strong>Note:</strong> Any pre-release identifiers are currently ignored, but this may change in the future.
     *
     * @param semver1 first semantic version
     * @param semver2 second semantic version
     * @return a negative integer, zero, or a positive integer as the first version is less than, equal to, or greater than the second
     */
    public static int compare(String semver1, String semver2) {
        int[] versions1 = parse(semver1);
        int[] versions2 = parse(semver2);

        for (int i = 0; i < 3; i++) {
            int diff = versions1[i] - versions2[i];
            if (diff != 0) return diff;
        }
        return 0;
    }

}
