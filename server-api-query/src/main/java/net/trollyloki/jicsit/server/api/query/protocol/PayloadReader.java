package net.trollyloki.jicsit.server.api.query.protocol;

import org.jspecify.annotations.NullMarked;

import java.nio.ByteBuffer;

/**
 * A function for reading the payload of a message.
 *
 * @param <T> payload type
 */
@NullMarked
@FunctionalInterface
public interface PayloadReader<T extends Payload> {

    /**
     * Reads a payload from a buffer.
     *
     * @param buffer byte buffer
     * @return payload
     */
    T read(ByteBuffer buffer);

}
