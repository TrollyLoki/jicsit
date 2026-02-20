package net.trollyloki.jicsit.server.api.query;

import net.trollyloki.jicsit.server.api.query.protocol.Message;
import net.trollyloki.jicsit.server.api.query.protocol.PayloadReader;
import net.trollyloki.jicsit.server.api.query.protocol.payload.CookiePayload;
import net.trollyloki.jicsit.server.api.query.protocol.payload.ServerStatePayload;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * An interface for the standard Lightweight Query API flow.
 */
public class LightweightQueryApi {

    /**
     * A request sent to the server to retrieve information about the current server state.
     */
    private static final byte POLL_SERVER_STATE = 0;

    /**
     * A response sent by the server containing the current server state.
     */
    private static final byte SERVER_STATE_RESPONSE = 1;

    private static final Map<Byte, PayloadReader<?>> PAYLOAD_READERS = Map.of(
            POLL_SERVER_STATE, CookiePayload::read,
            SERVER_STATE_RESPONSE, ServerStatePayload::read
    );

    private final LightweightQueryApiClient client;

    /**
     * Creates an interface for the standard Lightweight Query API flow on top of a {@link LightweightQueryApiClient}.
     *
     * @param client underlying client
     */
    public LightweightQueryApi(LightweightQueryApiClient client) {
        this.client = client;
    }

    /**
     * Polls the server for its current state.
     *
     * @param cookie unique identifier for the request
     * @return current server state
     * @throws java.nio.BufferOverflowException  if the request does not fit within the buffer size
     * @throws java.nio.BufferUnderflowException if the response did not fit within the buffer size
     * @throws java.net.SocketTimeoutException   if the timeout expires
     * @throws ProtocolException                 if a protocol error occurs
     * @throws IOException                       if an I/O error occurs or the socket is closed
     */
    public ServerState pollServerState(long cookie) throws IOException {
        client.send(new Message(POLL_SERVER_STATE, new CookiePayload(cookie)));

        // wait for the response
        while (true) {
            Message response = client.receive(PAYLOAD_READERS);

            if (!(response.payload() instanceof ServerStatePayload payload))
                continue; // ignore responses with unexpected type

            if (payload.cookie() != cookie)
                continue; // ignore responses with different cookie

            return payload.state();
        }
    }

    /**
     * Polls the server for its current state using a random number as the cookie.
     *
     * @return current server state
     * @throws java.nio.BufferOverflowException  if the request does not fit within the buffer size
     * @throws java.nio.BufferUnderflowException if the response did not fit within the buffer size
     * @throws java.net.SocketTimeoutException   if the timeout expires
     * @throws ProtocolException                 if a protocol error occurs
     * @throws IOException                       if an I/O error occurs or the socket is closed
     */
    public ServerState pollServerState() throws IOException {
        return pollServerState(ThreadLocalRandom.current().nextLong());
    }

}
