package net.trollyloki.jicsit.save;

import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * A session.
 *
 * @param sessionName name of the session
 * @param saveHeaders headers of all saves belonging to the session
 */
@NullMarked
public record Session(String sessionName, List<SaveHeader> saveHeaders) {

}
