package net.trollyloki.jicsit.save.modded;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Information about a loaded mod.
 *
 * @param reference mod reference
 * @param name      display name
 * @param version   version of the mod that was loaded when the save was created
 */
public record Mod(
        @JsonProperty("Reference") String reference,
        @JsonProperty("Name") String name,
        @JsonProperty("Version") String version
) {
}
