package net.trollyloki.jicsit.save;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Modded save metadata.
 *
 * @param version     metadata version
 * @param mods        list of mods that were loaded when the save was created
 * @param fullMapName full path to the map used by the save
 */
@NullMarked
public record ModMetadata(
        @JsonProperty("Version") int version,
        @JsonProperty("Mods") List<Mod> mods,
        @JsonProperty("FullMapName") @Nullable String fullMapName
) {
}
