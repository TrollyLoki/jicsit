package net.trollyloki.jicsit.server.api.query;

import net.trollyloki.jicsit.server.api.query.protocol.Message;
import net.trollyloki.jicsit.server.api.query.protocol.payload.CookiePayload;
import net.trollyloki.jicsit.server.api.query.protocol.payload.ServerStatePayload;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * An interface for the standard Lightweight Query API flow.
 */
public class LightweightQueryApi {

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
        client.send(new Message(Message.POLL_SERVER_STATE, new CookiePayload(cookie)));

        Message response;
        do {
            response = client.receive();
        } while (response.type() != Message.SERVER_STATE_RESPONSE
                || ((ServerStatePayload) response.payload()).cookie() != cookie
        );

        return ((ServerStatePayload) response.payload()).state();
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
