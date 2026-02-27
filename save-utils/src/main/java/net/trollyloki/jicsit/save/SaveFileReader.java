package net.trollyloki.jicsit.save;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
public final class SaveFileReader {
    private SaveFileReader() {
    }

    private static final Instant TICKS_ORIGIN = ZonedDateTime.of(1, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
    private static final long TICKS_PER_MILLIS = 10_000;
    private static final long NANOS_PER_TICK = 1_000_000 / TICKS_PER_MILLIS;

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
     * Reads information about a save file.
     *
     * @param filePath save file path
     * @return {@link SaveFileInfo}
     * @throws IOException if an error occurs
     * @see #readInfo(InputStream)
     */
    public static SaveFileInfo readInfo(Path filePath) throws IOException {
        try (InputStream stream = Files.newInputStream(filePath)) {
            return readInfo(stream);
        }
    }

    /**
     * Reads information about a save file.
     *
     * @param stream stream containing the save file data
     * @return {@link SaveFileInfo}
     * @throws IOException if an error occurs
     */
    public static SaveFileInfo readInfo(InputStream stream) throws IOException {

        int headerVersion = readInt(stream);
        if (headerVersion < 0 || headerVersion > 14) {
            throw new SaveFormatException("Unknown header version: " + headerVersion);
        } else if (headerVersion < 10) {
            throw new SaveFormatException("Unsupported header version: " + headerVersion);
        }

        int saveVersion = readInt(stream);
        int buildVersion = readInt(stream);
        String saveName = headerVersion >= 14 ? readString(stream) : null;
        String mapName = readString(stream);
        String mapOptions = readString(stream);
        String sessionName = readString(stream);
        Duration playDuration = Duration.ofSeconds(readInt(stream));
        Instant saveTimestamp = ticksToInstant(readLong(stream));
        byte sessionVisibility = readByte(stream);
        int editorObjectVersion = readInt(stream);
        String modMetadata = readString(stream);
        int modFlags = readInt(stream);
        String guid = readString(stream);

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
                hash = digest.digest(stream.readAllBytes());
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
                sessionVisibility,
                editorObjectVersion,
                modMetadata,
                modFlags,
                guid
        );
    }

    private static ByteBuffer buffer(InputStream stream, int len) throws IOException {
        return ByteBuffer.allocateDirect(len).put(stream.readNBytes(len)).rewind().order(ByteOrder.LITTLE_ENDIAN);
    }

    private static byte readByte(InputStream stream) throws IOException {
        return stream.readNBytes(1)[0];
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

        String string = new String(stream.readNBytes(length), charset);

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

        return stream.readNBytes(16);
    }

}
