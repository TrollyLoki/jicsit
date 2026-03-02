package net.trollyloki.jicsit.server.api.https;

import net.trollyloki.jicsit.save.Session;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * The sessions available on the server.
 *
 * @param sessions            list of sessions
 * @param currentSessionIndex index of the currently selected session in the list
 */
@NullMarked
public record ServerSessions(List<Session> sessions, int currentSessionIndex) {

    /**
     * Gets the currently selected session.
     *
     * @return current session, or {@code null} if no session is selected
     */
    public @Nullable Session current() {
        if (currentSessionIndex < 0) {
            return null;
        }
        return sessions.get(currentSessionIndex);
    }

}
