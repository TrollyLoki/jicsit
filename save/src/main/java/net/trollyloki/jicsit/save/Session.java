package net.trollyloki.jicsit.save;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * A session.
 *
 * @param sessionName name of the session
 * @param saveHeaders headers of all saves belonging to the session
 */
@NullMarked
public record Session(String sessionName, List<SaveHeader> saveHeaders) {

    /**
     * Finds a specific header in the list of saves belonging to this session.
     *
     * @param saveName name of the save (without any file extension)
     * @return save header, or {@code null} if not found
     */
    public @Nullable SaveHeader find(String saveName) {
        for (SaveHeader header : saveHeaders) {
            if (header.saveName().equals(saveName)) {
                return header;
            }
        }
        return null;
    }

}
