package net.trollyloki.jicsit.server.api.query.protocol;

import net.trollyloki.jicsit.server.api.query.ProtocolException;
import net.trollyloki.jicsit.server.api.query.protocol.payload.RawPayload;
import org.jspecify.annotations.NullMarked;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

/**
 * A message.
 *
 * @param type    message type ID
 * @param payload payload
 */
@NullMarked
public record Message(byte type, Payload payload) {

    private static final short PROTOCOL_MAGIC = (short) 0xF6D5;
    private static final byte TERMINATOR = 0x1;

    private static final byte PROTOCOL_VERSION = 1;

    /**
     * Writes the message to a buffer.
     *
     * @param buffer byte buffer
     * @throws java.nio.ReadOnlyBufferException if the buffer is read-only
     * @throws java.nio.BufferOverflowException if the buffer did not have space for the data
     */
    public void write(ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort(PROTOCOL_MAGIC);
        buffer.put(type);
        buffer.put(PROTOCOL_VERSION);
        payload.write(buffer);
        buffer.put(TERMINATOR);
    }

    /**
     * Reads a message from a buffer.
     *
     * @param buffer byte buffer
     * @param payloadReaders map of message types to payload readers
     * @return message
     * @throws ProtocolException                 if there is a protocol error
     * @throws java.nio.BufferUnderflowException if the buffer does not contain the expected amount of data
     */
    public static Message read(ByteBuffer buffer, Map<Byte, PayloadReader<?>> payloadReaders) throws ProtocolException {
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        short magic = buffer.getShort();
        if (magic != PROTOCOL_MAGIC) {
            throw new ProtocolException("Incorrect protocol magic: " + magic);
        }

        byte type = buffer.get();
        PayloadReader<?> payloadReader = payloadReaders.getOrDefault(type, RawPayload::read);

        byte protocolVersion = buffer.get();
        if (protocolVersion != PROTOCOL_VERSION) {
            throw new ProtocolException("Incorrect protocol version: " + protocolVersion);
        }

        Payload payload = payloadReader.read(buffer);

        byte terminator = buffer.get();
        if (terminator != TERMINATOR) {
            throw new ProtocolException("Incorrect terminator: " + terminator);
        }

        if (buffer.hasRemaining()) {
            throw new ProtocolException("Unexpected trailing data: (" + buffer.remaining() + " bytes)");
        }

        return new Message(type, payload);
    }

}
