package net.trollyloki.jicsit.server.api.https;

import net.trollyloki.jicsit.save.Session;

import java.util.List;

/**
 * The sessions available on the server.
 *
 * @param sessions            list of sessions
 * @param currentSessionIndex index of the currently selected session in the list
 */
public record ServerSessions(List<Session> sessions, int currentSessionIndex) {

    /**
     * Gets the currently selected session.
     *
     * @return current session
     */
    public Session current() {
        return sessions.get(currentSessionIndex);
    }

}
