package net.trollyloki.jicsit.server.api.query;

import net.trollyloki.jicsit.server.api.query.protocol.Message;
import net.trollyloki.jicsit.server.api.query.protocol.payload.CookiePayload;
import net.trollyloki.jicsit.server.api.query.protocol.payload.ServerStatePayload;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A client for the dedicated server Lightweight Query API.
 */
public class LightweightQueryApiClient implements Closeable {

    private final InetSocketAddress address;
    private int bufferSize = 1024;
    private final DatagramSocket socket;

    /**
     * Creates a new client.
     *
     * @param host server host name
     * @param port server port
     * @throws IllegalArgumentException if {@code port} is invalid, or {@code host} is {@code null} or cannot be resolved
     * @throws SocketException          if the socket could not be opened
     */
    public LightweightQueryApiClient(String host, int port) throws SocketException {
        this.address = new InetSocketAddress(host, port);
        if (address.isUnresolved()) {
            throw new IllegalArgumentException("Host name could not be resolved");
        }
        this.socket = new DatagramSocket();
    }

    /**
     * Gets the amount of bytes to allocate for messages.
     * <p>
     * <strong>Attempting to send/receive messages longer than this will fail.</strong>
     *
     * @return message buffer length
     */
    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * Sets the amount of bytes to allocate for messages.
     *
     * @param bufferSize message buffer length
     * @throws IllegalArgumentException if {@code bufferSize} is negative
     * @see #getBufferSize()
     */
    public void setBufferSize(int bufferSize) {
        if (bufferSize < 0) throw new IllegalArgumentException("Buffer size cannot be negative");
        this.bufferSize = bufferSize;
    }

    /**
     * Gets the timeout for receiving messages.
     *
     * @return timeout in milliseconds
     * @throws SocketException if the socket is closed or there is an underlying error
     */
    public int getTimeout() throws SocketException {
        return socket.getSoTimeout();
    }

    /**
     * Sets the timeout for receiving messages.
     *
     * @param timeout timeout in milliseconds
     * @throws IllegalArgumentException if {@code timeout} is negative
     * @throws SocketException          if the socket is closed or there is an underlying error
     */
    public void setTimeout(int timeout) throws SocketException {
        socket.setSoTimeout(timeout);
    }

    /**
     * Closes the socket.
     */
    @Override
    public void close() {
        socket.close();
    }

    /**
     * Checks if the socket is closed.
     *
     * @return {@code true} if the socket has been closed
     */
    public boolean isClosed() {
        return socket.isClosed();
    }

    private ByteBuffer newBuffer() {
        return ByteBuffer.allocate(bufferSize);
    }

    /**
     * Sends a message to the server.
     *
     * @param message message
     * @throws java.nio.BufferOverflowException if the message does not fit within the {@link #getBufferSize() buffer size}
     * @throws IOException                      if an I/O error occurs or the socket is closed
     */
    protected void send(Message message) throws IOException {
        ByteBuffer buffer = newBuffer();
        message.write(buffer);
        DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.position(), address);
        socket.send(packet);
    }

    /**
     * Attempts to receive a message from the server.
     *
     * @return message
     * @throws java.nio.BufferUnderflowException if the message did not fit within the {@link #getBufferSize() buffer size}
     * @throws java.net.SocketTimeoutException   if the {@link #getTimeout() timeout} expires
     * @throws ProtocolException                 if a protocol error occurs
     * @throws IOException                       if an I/O error occurs or the socket is closed
     */
    protected Message receive() throws IOException {
        ByteBuffer buffer = newBuffer();
        DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.capacity());
        socket.receive(packet);
        buffer.limit(packet.getLength());
        return Message.read(buffer);
    }

    /**
     * Polls the server for its current state.
     *
     * @param cookie unique identifier for the request
     * @return current server state
     * @throws java.nio.BufferOverflowException  if the request does not fit within the {@link #getBufferSize() buffer size}
     * @throws java.nio.BufferUnderflowException if the response did not fit within the {@link #getBufferSize() buffer size}
     * @throws java.net.SocketTimeoutException   if the {@link #getTimeout() timeout} expires
     * @throws ProtocolException                 if a protocol error occurs
     * @throws IOException                       if an I/O error occurs or the socket is closed
     */
    public ServerState pollServerState(long cookie) throws IOException {
        send(new Message(Message.POLL_SERVER_STATE, new CookiePayload(cookie)));

        Message response;
        do {
            response = receive();
        } while (response.type() != Message.SERVER_STATE_RESPONSE
                || ((ServerStatePayload) response.payload()).cookie() != cookie
        );

        return ((ServerStatePayload) response.payload()).state();
    }

    /**
     * Polls the server for its current state using a random number as the cookie.
     *
     * @return current server state
     * @throws java.nio.BufferOverflowException  if the request does not fit within the {@link #getBufferSize() buffer size}
     * @throws java.nio.BufferUnderflowException if the response did not fit within the {@link #getBufferSize() buffer size}
     * @throws java.net.SocketTimeoutException   if the {@link #getTimeout() timeout} expires
     * @throws ProtocolException                 if a protocol error occurs
     * @throws IOException                       if an I/O error occurs or the socket is closed
     */
    public ServerState pollServerState() throws IOException {
        return pollServerState(ThreadLocalRandom.current().nextLong());
    }

}
