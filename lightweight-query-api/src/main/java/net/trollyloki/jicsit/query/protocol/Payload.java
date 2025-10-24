package net.trollyloki.jicsit.query.protocol;

import java.nio.ByteBuffer;

/**
 * A payload for a message.
 */
public interface Payload {

    /**
     * Writes the payload to a buffer.
     *
     * @param buffer byte buffer
     */
    void write(ByteBuffer buffer);

}
