package net.trollyloki.jicsit.server.query;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketException;
import java.time.Duration;

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
        QueryClient client = new QueryClient(host, port);
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
    ServerState pollServerState(long cookie) throws IOException;

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
    ServerState pollServerState() throws IOException;

}
