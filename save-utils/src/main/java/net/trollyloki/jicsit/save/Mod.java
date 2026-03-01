package net.trollyloki.jicsit.save;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NullMarked;

/**
 * Information about a loaded mod.
 *
 * @param reference mod reference
 * @param name      display name
 * @param version   version of the mod that was loaded when the save was created
 */
@NullMarked
public record Mod(
        @JsonProperty("Reference") String reference,
        @JsonProperty("Name") String name,
        @JsonProperty("Version") String version
) {
}
