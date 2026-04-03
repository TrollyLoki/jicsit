package net.trollyloki.jicsit.save;

import org.jspecify.annotations.NullMarked;

import java.util.Arrays;

/**
 * A 128-bit MD5 hash.
 *
 * @param bytes hash bytes
 */
@NullMarked
public record MD5Hash(byte[] bytes) {

    /**
     * Creates a new {@link MD5Hash} instance from the bytes of an MD5 hash.
     *
     * @param bytes hash bytes
     */
    public MD5Hash(byte[] bytes) {
        if (bytes.length != 16)
            throw new IllegalArgumentException("MD5 hashes have exactly 16 bytes");
        this.bytes = bytes.clone();
    }

    /**
     * Gets a copy of the bytes of this MD5 hash.
     *
     * @return hash bytes
     */
    @Override
    public byte[] bytes() {
        return this.bytes.clone();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MD5Hash md5Hash)) return false;
        return Arrays.equals(this.bytes, md5Hash.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.bytes);
    }

}
