package net.trollyloki.jicsit.query;

import java.io.IOException;

/**
 * A lightweight query protocol error.
 */
public class ProtocolException extends IOException {

    /**
     * Creates a new protocol exception with the specified detail message.
     *
     * @param message detail message
     */
    public ProtocolException(String message) {
        super(message);
    }

}
