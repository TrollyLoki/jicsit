package net.trollyloki.jicsit.save;

import java.util.Map;

public record MockSaveFileValues(
        SaveFileInfo info,
        Map<String, String> parsedMapOptions,
        boolean modMetadataValid,
        ModMetadata parsedModMetadata
) {
}
