package net.trollyloki.jicsit.save;

/**
 * Information about a save file.
 *
 * @param header        {@link SaveHeader}
 * @param visibility    session visibility (irrelevant since 1.0)
 * @param objectVersion editor object version
 * @param modMetadata   mod metadata, or an empty string if the save is unmodded
 * @param modFlags      mod flags, or {@code 0} if the save is unmodded
 * @param identifier    GUID for the session (regenerated only when creating a new game)
 */
public record SaveFileInfo(
        SaveHeader header,
        byte visibility,
        int objectVersion,
        String modMetadata,
        int modFlags,
        String identifier
) {

}
