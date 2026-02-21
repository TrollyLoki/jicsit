package net.trollyloki.jicsit.save;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic information about a save.
 *
 * @param saveVersion                   save game format version
 * @param buildVersion                  build version of the game/server that created the save
 * @param saveName                      name of the save (without any file extension)
 * @param mapName                       name of the map used by the save
 * @param mapOptions                    additional {@link #parseMapOptions() map options}
 * @param sessionName                   name of the session that the save belongs to
 * @param playDurationSeconds           amount of time that the session has been running for in total
 * @param saveDateTime                  instant when the save was created
 * @param isModded                      {@code true} if the save has been modded
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
     * @return parsed map options
     * @see #mapOptions()
     */
    public Map<String, String> parseMapOptions() {
        Map<String, String> map = new HashMap<>();
        if (mapOptions != null) {
            for (String property : mapOptions.split("\\?")) {
                if (property.isEmpty()) continue;

                int splitAt = property.indexOf('=');
                if (splitAt < 0) {
                    map.put(property, null);
                } else {
                    map.put(property.substring(0, splitAt), property.substring(splitAt + 1));
                }
            }
        }
        return map;
    }

}
