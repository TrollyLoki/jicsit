package net.trollyloki.jicsit.server.api.query.protocol;

import org.jspecify.annotations.NullMarked;

import java.nio.ByteBuffer;

/**
 * A payload for a message.
 */
@NullMarked
public interface Payload {

    /**
     * Writes the payload to a buffer.
     *
     * @param buffer byte buffer
     */
    void write(ByteBuffer buffer);

}
