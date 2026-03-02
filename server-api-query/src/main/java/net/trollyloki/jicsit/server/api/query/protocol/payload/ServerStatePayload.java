package net.trollyloki.jicsit.server.api.query.protocol.payload;

import net.trollyloki.jicsit.server.api.query.ServerState;
import net.trollyloki.jicsit.server.api.query.ServerStatus;
import net.trollyloki.jicsit.server.api.query.ServerSubState;
import net.trollyloki.jicsit.server.api.query.protocol.Payload;
import org.jspecify.annotations.NullMarked;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * A payload containing a cookie and the server state.
 *
 * @param cookie unique identifier for the request
 * @param state  server state
 */
@NullMarked
public record ServerStatePayload(long cookie, ServerState state) implements Payload {

    @Override
    public void write(ByteBuffer buffer) {
        buffer.putLong(cookie);
        buffer.put((byte) state.status().ordinal());
        buffer.putInt(state.build());
        buffer.putLong(state.flags());
        buffer.put((byte) state.subStates().size());
        for (ServerSubState subState : state.subStates()) {
            buffer.put(subState.id());
            buffer.putShort(subState.version());
        }
        buffer.putShort((short) state.name().length());
        buffer.put(state.name().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Reads a server state payload from a buffer.
     *
     * @param buffer byte buffer
     * @return server state payload
     */
    public static ServerStatePayload read(ByteBuffer buffer) {
        return new ServerStatePayload(buffer.getLong(), new ServerState(
                ServerStatus.values()[buffer.get()],
                buffer.getInt(),
                buffer.getLong(),
                readSubStates(buffer),
                readName(buffer)
        ));
    }

    private static List<ServerSubState> readSubStates(ByteBuffer buffer) {
        ServerSubState[] subStates = new ServerSubState[buffer.get()];
        for (int i = 0; i < subStates.length; i++) {
            subStates[i] = new ServerSubState(buffer.get(), buffer.getShort());
        }
        return List.of(subStates);
    }

    private static String readName(ByteBuffer buffer) {
        byte[] array = new byte[buffer.getShort()];
        buffer.get(array);
        return new String(array, StandardCharsets.UTF_8);
    }

}
