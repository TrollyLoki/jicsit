package net.trollyloki.jicsit.save;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * A save file's header information.
 *
 * @param saveVersion                   save game format version
 * @param buildVersion                  build version of the game/server the file was saved by
 * @param saveName                      name of the save file (without the extension)
 * @param mapName                       path name to the map package the save is based on
 * @param mapOptions                    additional map URL options
 * @param sessionName                   name of the session the save belongs to
 * @param playDurationSeconds           amount of seconds the game has been running for in total since the session was created
 * @param saveDateTime                  instant when the save was made
 * @param isModded                      {@code true} if the file was saved with mods
 * @param isEdited                      {@code true} if the save has been edited by third party tools
 * @param isAdvancedGameSettingsEnabled {@code true} if Advanced Game Settings are enabled for the save
 */
public record SaveHeader(
        int saveVersion,
        int buildVersion,
        String saveName,
        String mapName,
        String mapOptions,
        String sessionName,
        int playDurationSeconds,
        @JsonFormat(pattern = "yyyy.MM.dd-HH.mm.ss", timezone = "UTC") Instant saveDateTime,
        @JsonProperty("isModdedSave") boolean isModded,
        @JsonProperty("isEditedSave") boolean isEdited,
        @JsonProperty("isCreativeModeEnabled") boolean isAdvancedGameSettingsEnabled
) {

    /**
     * Gets the duration the game has been running for in total since the session was created.
     *
     * @return play duration
     * @see #playDurationSeconds()
     */
    public Duration playDuration() {
        return Duration.ofSeconds(playDurationSeconds);
    }

    /**
     * Attempts to parse individual map options out of the map options string.
     *
     * @return parsed map options, may be empty
     * @see #mapOptions()
     */
    public Map<String, String> parseMapOptions() {
        Map<String, String> map = new HashMap<>();
        if (mapOptions != null) {
            for (String property : mapOptions.split("\\?")) {
                if (property.isEmpty()) continue;
                String[] split = property.split("=");
                map.put(split[0], split.length > 1 ? split[1] : null);
            }
        }
        return map;
    }

}
