package net.trollyloki.jicsit.save;

import java.io.IOException;

/**
 * An error caused by unparsable save file data.
 */
public class SaveFormatException extends IOException {

    /**
     * Creates a new save format exception with the specified detail message.
     *
     * @param message detail message
     */
    public SaveFormatException(String message) {
        super(message);
    }

    /**
     * Creates a new save format exception with the specified detail message and cause.
     *
     * @param message detail message
     * @param cause   cause
     */
    public SaveFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}
