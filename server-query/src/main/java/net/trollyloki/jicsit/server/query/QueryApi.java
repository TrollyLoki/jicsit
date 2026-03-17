package net.trollyloki.jicsit.server.query;

import net.trollyloki.jicsit.server.query.protocol.Message;
import net.trollyloki.jicsit.server.query.protocol.PayloadReader;
import net.trollyloki.jicsit.server.query.protocol.payload.CookiePayload;
import net.trollyloki.jicsit.server.query.protocol.payload.ServerStatePayload;
import org.jspecify.annotations.NullMarked;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * An interface for the standard Lightweight Query API flow.
 */
@NullMarked
public class QueryApi implements Closeable {

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

    private final QueryApiClient client;

    /**
     * Creates an interface for the standard Lightweight Query API flow on top of a {@link QueryApiClient}.
     *
     * @param client underlying client
     */
    public QueryApi(QueryApiClient client) {
        this.client = client;
    }

    /**
     * Closes the underlying client's socket.
     */
    @Override
    public void close() {
        client.close();
    }

    /**
     * Creates a new {@link QueryApi} instance for interfacing with
     * the standard Lightweight Query API of a specific server.
     *
     * @param host    server host name
     * @param port    server port
     * @param timeout timeout for receiving messages in milliseconds
     * @return new {@link QueryApi} instance
     * @throws IllegalArgumentException if {@code port} is invalid, {@code host} is {@code null}, or {@code timeout} is negative
     * @throws SocketException          if the socket could not be opened
     * @see QueryApiClient#QueryApiClient(String, int)
     * @see QueryApi#QueryApi(QueryApiClient)
     */
    public static QueryApi of(String host, int port, int timeout) throws SocketException {
        QueryApiClient client = new QueryApiClient(host, port);
        client.setTimeout(timeout);
        return new QueryApi(client);
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
