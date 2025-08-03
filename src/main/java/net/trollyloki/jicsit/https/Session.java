package net.trollyloki.jicsit.https;


import java.util.List;

/**
 * A session.
 *
 * @param sessionName name of the session
 * @param saveHeaders headers of all saves belonging to the session
 */
public record Session(String sessionName, List<SaveHeader> saveHeaders) {

}
