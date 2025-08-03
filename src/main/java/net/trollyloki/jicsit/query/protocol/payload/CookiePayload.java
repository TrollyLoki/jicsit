package net.trollyloki.jicsit.query.protocol.payload;

import net.trollyloki.jicsit.query.protocol.Payload;

import java.nio.ByteBuffer;

/**
 * A payload containing just a cookie.
 *
 * @param cookie unique identifier for the request
 */
public record CookiePayload(long cookie) implements Payload {

    @Override
    public void write(ByteBuffer buffer) {
        buffer.putLong(cookie);
    }

    /**
     * Reads a cookie payload from a buffer.
     *
     * @param buffer byte buffer
     * @return cookie payload
     */
    public static CookiePayload read(ByteBuffer buffer) {
        return new CookiePayload(buffer.getLong());
    }

}
