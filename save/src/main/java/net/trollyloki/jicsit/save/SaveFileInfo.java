package net.trollyloki.jicsit.save;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * Information about a save file.
 *
 * @param headerVersion       save header format version
 * @param header              {@link SaveHeader}
 * @param originalSaveName    original save name (only used by consoles), or {@code null} if unknown
 * @param sessionVisibility   session visibility (ignored since 1.0)
 * @param editorObjectVersion editor object version, or {@code 0} if unknown
 * @param modMetadata         mod metadata, or {@code null} if unknown
 * @param modFlags            mod flags, or {@code 0} if the save is unmodded
 * @param guid                GUID for the session (regenerated only when creating a new game), or {@code null} if unknown
 * @param checksum            MD5 hash of the original save data following the header, or {@code null} if unknown
 */
@NullMarked
public record SaveFileInfo(
        int headerVersion,
        SaveHeader header,
        @Nullable String originalSaveName,
        byte sessionVisibility,
        int editorObjectVersion,
        @Nullable String modMetadata,
        int modFlags,
        @Nullable String guid,
        @Nullable MD5Hash checksum
) {

    private static final JsonMapper MOD_METADATA_MAPPER = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES).build();

    /**
     * Attempts to parse the mod metadata.
     *
     * @return parsed mod metadata, or {@code null} if the save is unmodded
     * @throws SaveFormatException if the mod metadata is invalid
     * @see #modMetadata()
     */
    public @Nullable ModMetadata parseModMetadata() throws SaveFormatException {
        if (modMetadata == null || modMetadata.isEmpty())
            return null;

        try {
            return MOD_METADATA_MAPPER.readValue(modMetadata, ModMetadata.class);
        } catch (JacksonException e) {
            throw new SaveFormatException("Invalid mod metadata", e);
        }
    }

}
