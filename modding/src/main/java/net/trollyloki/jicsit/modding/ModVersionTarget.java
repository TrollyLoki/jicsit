package net.trollyloki.jicsit.modding;

import org.jspecify.annotations.NullMarked;

/**
 * Information about the archive for a specific mod version and installation target.
 *
 * @param versionId SMR version ID
 * @param targetName installation target
 * @param hash hash of the archive
 * @param size size of the archive
 */
@NullMarked
public record ModVersionTarget(
        String versionId,
        String targetName,
        String hash,
        long size
) {
}
