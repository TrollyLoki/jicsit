package net.trollyloki.jicsit.save;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;

/**
 * Utility class for reading save files.
 */
@NullMarked
public final class SaveFileReader {
    private SaveFileReader() {
    }

    private static final Instant TICKS_ORIGIN = ZonedDateTime.of(1, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
    private static final long TICKS_PER_MILLIS = 10_000;
    private static final long NANOS_PER_TICK = 1_000_000 / TICKS_PER_MILLIS;

    private static int maxStringBytes = 8192;

    /**
     * Sets the maximum number of bytes in a string value.
     * <p>
     * If reading any string value would require buffering more than this many bytes,
     * {@link #readInfo(Path)} and {@link #readInfo(String, InputStream)} will throw a {@link SaveFormatException}.
     *
     * @param numBytes maximum number of bytes
     */
    public static void setMaxStringBytes(int numBytes) {
        maxStringBytes = numBytes;
    }

    /**
     * Converts a .NET <a href="https://learn.microsoft.com/en-us/dotnet/api/system.datetime.ticks">DateTime.Ticks</a>
     * value to a Java {@link Instant}.
     *
     * @param ticks number of ticks
     * @return instant
     */
    public static Instant ticksToInstant(long ticks) {
        return TICKS_ORIGIN
                .plusMillis(ticks / TICKS_PER_MILLIS)
                .plusNanos((ticks % TICKS_PER_MILLIS) * NANOS_PER_TICK);
    }

    /**
     * File extension used for save files (including the dot).
     */
    public static final String EXTENSION = ".sav";

    /**
     * Gets the save name displayed for a save file.
     * <p>
     * <strong>Note:</strong> The game ignores any files that do not have the ".sav" file extension.
     *
     * @param filename save file name
     * @return save name
     */
    public static String saveNameOf(String filename) {
        if (filename.endsWith(EXTENSION)) {
            return filename.substring(0, filename.length() - EXTENSION.length());
        }
        return filename;
    }

    /**
     * Gets the save name displayed for a save file.
     * <p>
     * <strong>Note:</strong> The game ignores any files that do not have the ".sav" file extension.
     *
     * @param filePath save file path
     * @return save name
     * @throws IllegalArgumentException if the save file path is empty
     */
    public static String saveNameOf(Path filePath) {
        Path filenamePath = filePath.getFileName();
        if (filenamePath == null) {
            throw new IllegalArgumentException("Save file path cannot be empty");
        }
        return saveNameOf(filenamePath.toString());
    }

    /**
     * Reads information about a save file.
     *
     * @param filePath save file path
     * @return {@link SaveFileInfo}
     * @throws IOException if an error occurs
     * @see #readInfo(String, InputStream)
     */
    public static SaveFileInfo readInfo(Path filePath) throws IOException {
        try (InputStream stream = Files.newInputStream(filePath)) {
            return readInfo(saveNameOf(filePath), stream);
        }
    }

    /**
     * Reads information about a save file.
     *
     * @param saveName {@link SaveHeader#saveName() save name}
     * @param stream   stream containing the save file data
     * @return {@link SaveFileInfo}
     * @throws IOException if an error occurs
     */
    public static SaveFileInfo readInfo(String saveName, InputStream stream) throws IOException {

        int headerVersion = readInt(stream);
        if (headerVersion < 0) {
            throw new SaveFormatException("Invalid header version: " + headerVersion);
        } else if (headerVersion < 5) {
            throw new SaveFormatException("Unsupported header version: " + headerVersion);
        } else if (headerVersion > 14) {
            throw new SaveFormatException("Unknown header version: " + headerVersion);
        }

        int saveVersion = readInt(stream);
        int buildVersion = readInt(stream);
        String originalSaveName = headerVersion >= 14 ? readString(stream) : null;
        String mapName = readString(stream);
        String mapOptions = readString(stream);
        String sessionName = readString(stream);
        Duration playDuration = Duration.ofSeconds(readInt(stream));
        Instant saveTimestamp = ticksToInstant(readLong(stream));
        byte sessionVisibility = readByte(stream);
        int editorObjectVersion = headerVersion >= 7 ? readInt(stream) : 0;
        String modMetadata = headerVersion >= 8 ? readString(stream) : null;
        int modFlags = headerVersion >= 8 ? readInt(stream) : 0;
        String guid = headerVersion >= 10 ? readString(stream) : null;

        if (headerVersion >= 11) {
            int isPartitionedWorld = readInt(stream); // always 1
            if (isPartitionedWorld != 1) {
                throw new SaveFormatException("Invalid isPartitionedWorld value: " + isPartitionedWorld);
            }
        }

        byte[] checksum = headerVersion >= 12 ? readMD5Hash(stream) : null; // MD5 hash of all data after the header

        int isCreativeModeEnabled = 0;
        if (headerVersion >= 13) {
            isCreativeModeEnabled = readInt(stream);
            if (!(isCreativeModeEnabled == 0 || isCreativeModeEnabled == 1)) {
                throw new SaveFormatException("Invalid isCreativeModeEnabled value: " + isCreativeModeEnabled);
            }
        }

        byte[] hash = null;
        if (checksum != null) {
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");

                DigestInputStream digestStream = new DigestInputStream(stream, digest);
                byte[] buffer = new byte[8192];
                //noinspection StatementWithEmptyBody
                while (digestStream.read(buffer) != -1) {
                    // update digest
                }

                hash = digest.digest();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        SaveHeader header = new SaveHeader(
                saveVersion,
                buildVersion,
                saveName,
                mapName,
                mapOptions,
                sessionName,
                playDuration,
                saveTimestamp,
                modFlags != 0,
                !Arrays.equals(hash, checksum),
                isCreativeModeEnabled != 0
        );
        return new SaveFileInfo(
                headerVersion,
                header,
                originalSaveName,
                sessionVisibility,
                editorObjectVersion,
                modMetadata,
                modFlags,
                guid,
                checksum == null ? null : new MD5Hash(checksum)
        );
    }

    private static byte[] readNBytesOrFail(InputStream stream, int len) throws IOException {
        byte[] bytes = stream.readNBytes(len);
        if (bytes.length < len) {
            throw new SaveFormatException("Unexpected end of file");
        }
        return bytes;
    }

    private static ByteBuffer buffer(InputStream stream, int len) throws IOException {
        return ByteBuffer.allocateDirect(len).put(readNBytesOrFail(stream, len)).rewind().order(ByteOrder.LITTLE_ENDIAN);
    }

    private static byte readByte(InputStream stream) throws IOException {
        return readNBytesOrFail(stream, 1)[0];
    }

    private static int readInt(InputStream stream) throws IOException {
        return buffer(stream, 4).getInt();
    }

    private static long readLong(InputStream stream) throws IOException {
        return buffer(stream, 8).getLong();
    }

    private static String readString(InputStream stream) throws IOException {
        int length = readInt(stream);

        if (length == 0) return "";

        Charset charset = StandardCharsets.UTF_8;
        if (length < 0) {
            charset = StandardCharsets.UTF_16LE;
            length *= -2;
        }

        if (length <= 0) {
            throw new SaveFormatException("Invalid string length: " + length);
        } else if (length > maxStringBytes) {
            throw new SaveFormatException("Invalid string length: " + length + " > " + maxStringBytes
                    + " (if this is expected setMaxStringBytes(int) can be used to increase the limit)");
        }

        String string = new String(readNBytesOrFail(stream, length), charset);

        if (!string.endsWith("\0")) {
            throw new SaveFormatException("Invalid null terminator: " + string.charAt(string.length() - 1));
        }

        return string.substring(0, string.length() - 1);
    }

    private static byte[] readMD5Hash(InputStream stream) throws IOException {
        int isValid = readInt(stream); // always 1

        if (isValid != 1) {
            throw new SaveFormatException("Invalid MD5Hash: " + isValid);
        }

        return readNBytesOrFail(stream, 16);
    }

}
