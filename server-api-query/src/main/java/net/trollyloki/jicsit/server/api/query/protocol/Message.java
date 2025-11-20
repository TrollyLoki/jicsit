package net.trollyloki.jicsit.server.api.query.protocol;

import net.trollyloki.jicsit.server.api.query.ProtocolException;
import net.trollyloki.jicsit.server.api.query.protocol.payload.CookiePayload;
import net.trollyloki.jicsit.server.api.query.protocol.payload.RawPayload;
import net.trollyloki.jicsit.server.api.query.protocol.payload.ServerStatePayload;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A message.
 *
 * @param type    message type ID
 * @param payload payload
 */
public record Message(byte type, Payload payload) {

    private static final short PROTOCOL_MAGIC = (short) 0xF6D5;
    private static final byte TERMINATOR = 0x1;

    private static final byte PROTOCOL_VERSION = 1;

    /**
     * A request sent to the server to retrieve information about the current server state.
     */
    public static final byte POLL_SERVER_STATE = 0;

    /**
     * A response sent by the server containing the current server state.
     */
    public static final byte SERVER_STATE_RESPONSE = 1;

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
     * @return message
     * @throws ProtocolException                 if there is a protocol error
     * @throws java.nio.BufferUnderflowException if the buffer does not contain the expected amount of data
     */
    public static Message read(ByteBuffer buffer) throws ProtocolException {
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        short magic = buffer.getShort();
        if (magic != PROTOCOL_MAGIC) {
            throw new ProtocolException("Incorrect protocol magic: " + magic);
        }

        byte type = buffer.get();

        byte protocolVersion = buffer.get();
        if (protocolVersion != PROTOCOL_VERSION) {
            throw new ProtocolException("Incorrect protocol version: " + protocolVersion);
        }

        Payload payload = switch (type) {
            case POLL_SERVER_STATE -> CookiePayload.read(buffer);
            case SERVER_STATE_RESPONSE -> ServerStatePayload.read(buffer);
            default -> RawPayload.read(buffer);
        };

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
