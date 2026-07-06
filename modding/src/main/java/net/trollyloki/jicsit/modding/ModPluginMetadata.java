package net.trollyloki.jicsit.modding;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NullMarked;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * Metadata of a mod plugin.
 *
 * @param name friendly name of the mod
 * @param version semantic version
 * @param gameVersion game version requirement
 * @param isGameFeature {@code true} if the mod needs to be installed as a game feature
 */
@NullMarked
public record ModPluginMetadata(
        @JsonProperty("FriendlyName") String name,
        @JsonProperty("SemVersion") String version,
        @JsonProperty("GameVersion") String gameVersion,
        @JsonProperty("GameFeature") boolean isGameFeature
) implements Versioned {

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .build();

    /**
     * Reads metadata from a mod's uplugin file.
     *
     * @param content uplugin file content
     * @return {@link ModPluginMetadata}
     */
    public static ModPluginMetadata read(byte[] content) {
        return JSON_MAPPER.readValue(content, ModPluginMetadata.class);
    }

}
