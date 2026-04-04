package net.trollyloki.jicsit.server.query;

import net.trollyloki.jicsit.server.query.protocol.payload.ServerStatePayload;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/**
 * An interface for the vanilla Lightweight Query API flow.
 */
@NullMarked
public interface QueryApi extends Closeable {

    /**
     * Creates a new {@link QueryApi} instance for the vanilla Lightweight Query API on a specific server.
     *
     * @param host    server host name
     * @param port    server port
     * @param timeout duration to wait for responses, or {@code null} to wait indefinitely
     * @return new {@link QueryApi} instance
     * @throws IllegalArgumentException if {@code timeout} is non-positive or {@code host}/{@code port} is invalid
     * @throws SocketException          if the socket could not be opened
     * @see QueryClient#QueryClient(String, int)
     * @see QueryClient#setTimeout(int)
     */
    static QueryApi of(String host, int port, @Nullable Duration timeout) throws SocketException {
        return of(new QueryClient(host, port), timeout);
    }

    /**
     * Creates a new {@link QueryApi} instance for the vanilla Lightweight Query API on a specific server.
     *
     * @param host    server host address
     * @param port    server port
     * @param timeout duration to wait for responses, or {@code null} to wait indefinitely
     * @return new {@link QueryApi} instance
     * @throws IllegalArgumentException if {@code timeout} is non-positive or {@code host}/{@code port} is invalid
     * @throws SocketException          if the socket could not be opened
     * @see QueryClient#QueryClient(InetAddress, int)
     * @see QueryClient#setTimeout(int)
     */
    static QueryApi of(InetAddress host, int port, @Nullable Duration timeout) throws SocketException {
        return of(new QueryClient(host, port), timeout);
    }

    private static QueryApi of(QueryClient client, @Nullable Duration timeout) throws SocketException {
        if (timeout != null) {
            long millis = timeout.toMillis();
            if (millis <= 0) throw new IllegalArgumentException("Timeout duration must be positive");
            client.setTimeout((int) timeout.toMillis());
        }
        return of(client);
    }

    /**
     * Creates a new {@link QueryApi} instance for the vanilla Lightweight Query API on top of a {@link QueryClient}.
     *
     * @param client underlying client
     * @return new {@link QueryApi} instance
     */
    static QueryApi of(QueryClient client) {
        return new QueryApiImpl(client);
    }

    /**
     * Closes the underlying client's socket.
     */
    @Override
    void close();

    /**
     * Checks if the underlying client's socket is closed.
     *
     * @return {@code true} if the socket has been closed
     */
    boolean isClosed();

    /**
     * Sends a request to the server for its current state.
     * Responses must be received separately using {@link #receiveServerState()}.
     *
     * @param cookie unique identifier for the request
     * @throws java.nio.BufferOverflowException  if the request does not fit within the buffer size
     * @throws IOException                       if an I/O error occurs or the socket is closed
     * @see #pollServerState()
     */
    void requestServerState(long cookie) throws IOException;

    /**
     * Receives a response from the server with its current state.
     *
     * @return server state payload, including the request identifier and current server state
     * @throws java.nio.BufferUnderflowException if the response did not fit within the buffer size
     * @throws java.net.SocketTimeoutException   if the timeout expires
     * @throws ProtocolException                 if a protocol error occurs
     * @throws IOException                       if an I/O error occurs or the socket is closed
     * @see #requestServerState(long)
     */
    ServerStatePayload receiveServerState() throws IOException;

    /**
     * Polls the server for its current state.
     * <p>
     * Since the Lightweight Query API uses UDP, there is no guarantee that this will succeed even if the server is online.
     * Long-running applications should instead ping the server using {@link #requestServerState(long)}
     * at regular intervals and receive responses separately using {@link #receiveServerState()}.
     *
     * @return current server state
     * @throws java.nio.BufferOverflowException  if the request does not fit within the buffer size
     * @throws java.nio.BufferUnderflowException if the response did not fit within the buffer size
     * @throws java.net.SocketTimeoutException   if the timeout expires
     * @throws ProtocolException                 if a protocol error occurs
     * @throws IOException                       if an I/O error occurs or the socket is closed
     */
    default ServerState pollServerState() throws IOException {
        long cookie = ThreadLocalRandom.current().nextLong();

        requestServerState(cookie);

        ServerStatePayload response;
        do {
            response = receiveServerState();
        } while (response.cookie() != cookie);
        return response.state();
    }

}
