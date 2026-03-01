package net.trollyloki.jicsit.save;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Information about a save file.
 *
 * @param headerVersion       save header format version
 * @param header              {@link SaveHeader}
 * @param sessionVisibility   session visibility (ignored since 1.0)
 * @param editorObjectVersion editor object version, or {@code 0} if unknown
 * @param modMetadata         mod metadata, or {@code null} if unknown
 * @param modFlags            mod flags, or {@code 0} if the save is unmodded
 * @param guid                GUID for the session (regenerated only when creating a new game), or {@code null} if unknown
 */
public record SaveFileInfo(
        int headerVersion,
        SaveHeader header,
        byte sessionVisibility,
        int editorObjectVersion,
        String modMetadata,
        int modFlags,
        String guid
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
