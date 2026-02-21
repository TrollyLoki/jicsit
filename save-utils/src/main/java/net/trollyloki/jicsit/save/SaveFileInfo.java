package net.trollyloki.jicsit.save;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.trollyloki.jicsit.save.modded.ModMetadata;

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

    private static final ObjectMapper MOD_METADATA_MAPPER = new ObjectMapper();

    /**
     * Attempts to parse the mod metadata.
     *
     * @return parsed mod metadata, or {@code null} if the save is unmodded
     * @throws SaveFormatException if the mod metadata is invalid
     * @see #modMetadata()
     */
    public ModMetadata parseModMetadata() throws SaveFormatException {
        if (modMetadata.isEmpty())
            return null;

        try {
            return MOD_METADATA_MAPPER.readValue(modMetadata, ModMetadata.class);
        } catch (JsonProcessingException e) {
            throw new SaveFormatException("Invalid mod metadata", e);
        }
    }

}
