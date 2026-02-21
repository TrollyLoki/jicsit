package net.trollyloki.jicsit.save.modded;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Modded save metadata.
 *
 * @param version     metadata version
 * @param mods        list of mods that were loaded when the save was created
 * @param fullMapName full path to the map used by the save
 */
public record ModMetadata(
        @JsonProperty("Version") int version,
        @JsonProperty("Mods") List<Mod> mods,
        @JsonProperty("FullMapName") String fullMapName
) {
}
