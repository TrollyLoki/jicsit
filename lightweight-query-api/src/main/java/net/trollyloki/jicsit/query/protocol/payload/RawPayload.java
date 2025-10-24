package net.trollyloki.jicsit.query.protocol.payload;

import net.trollyloki.jicsit.query.protocol.Payload;

import java.nio.ByteBuffer;

/**
 * An unstructured payload.
 *
 * @param data payload data
 */
public record RawPayload(byte[] data) implements Payload {

    @Override
    public void write(ByteBuffer buffer) {
        buffer.put(data);
    }

    /**
     * Reads a raw payload from a buffer.
     *
     * @param buffer byte buffer
     * @return payload data
     */
    public static RawPayload read(ByteBuffer buffer) {
        if (buffer.remaining() <= 1) {
            // don't consume terminator byte
            return new RawPayload(new byte[0]);
        }
        byte[] data = new byte[buffer.remaining() - 1];
        buffer.get(data);
        return new RawPayload(data);
    }

}
