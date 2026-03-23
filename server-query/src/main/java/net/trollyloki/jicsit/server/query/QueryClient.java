package net.trollyloki.jicsit.server.query;

import net.trollyloki.jicsit.server.query.protocol.Message;
import net.trollyloki.jicsit.server.query.protocol.PayloadReader;
import org.jspecify.annotations.NullMarked;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * A client for the dedicated server Lightweight Query API.
 */
@NullMarked
public class QueryClient implements Closeable {

    private final InetSocketAddress address;
    private int bufferSize = 1024;
    private final DatagramSocket socket;

    private QueryClient(InetSocketAddress address) throws SocketException {
        this.address = address;
        if (address.isUnresolved())
            throw new IllegalArgumentException("Unresolved address");
        this.socket = new DatagramSocket();
    }

    /**
     * Creates a new client.
     *
     * @param host server host address
     * @param port server port
     * @throws IllegalArgumentException if {@code host}/{@code port} is invalid
     * @throws SocketException          if the socket could not be opened
     */
    public QueryClient(InetAddress host, int port) throws SocketException {
        this(new InetSocketAddress(host, port));
    }

    /**
     * Creates a new client.
     *
     * @param host server host name
     * @param port server port
     * @throws IllegalArgumentException if {@code host}/{@code port} is invalid
     * @throws SocketException          if the socket could not be opened
     */
    public QueryClient(String host, int port) throws SocketException {
        this(new InetSocketAddress(host, port));
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
     * @return timeout in milliseconds, or {@code 0} if infinite
     * @throws SocketException if the socket is closed or there is an underlying error
     */
    public int getTimeout() throws SocketException {
        return socket.getSoTimeout();
    }

    /**
     * Sets the timeout for receiving messages.
     *
     * @param timeout timeout in milliseconds, or {@code 0} for infinite
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
    public void send(Message message) throws IOException {
        ByteBuffer buffer = newBuffer();
        message.write(buffer);
        DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.position(), address);
        socket.send(packet);
    }

    /**
     * Attempts to receive a message from the server.
     *
     * @param payloadReaders map of message types to payload readers
     * @return message
     * @throws java.nio.BufferUnderflowException if the message did not fit within the {@link #getBufferSize() buffer size}
     * @throws java.net.SocketTimeoutException   if the {@link #getTimeout() timeout} expires
     * @throws ProtocolException                 if a protocol error occurs
     * @throws IOException                       if an I/O error occurs or the socket is closed
     */
    public Message receive(Map<Byte, PayloadReader<?>> payloadReaders) throws IOException {
        ByteBuffer buffer = newBuffer();
        DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.capacity());
        socket.receive(packet);
        buffer.limit(packet.getLength());
        return Message.read(buffer, payloadReaders);
    }

}
