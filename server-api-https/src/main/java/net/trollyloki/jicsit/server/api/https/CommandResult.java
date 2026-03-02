package net.trollyloki.jicsit.server.api.https;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NullMarked;

/**
 * The result of a console command.
 *
 * @param success {@code true} if the command executed successfully, or {@code false} if it did not
 * @param output  multiline output of the command
 */
@NullMarked
public record CommandResult(
        @JsonProperty("returnValue") boolean success,
        @JsonProperty("commandResult") String output
) {

    /**
     * Gets each line of {@link #output() output} as a separate string.
     *
     * @return array of output lines
     */
    public String[] outputLines() {
        return output.split("\n");
    }

}
