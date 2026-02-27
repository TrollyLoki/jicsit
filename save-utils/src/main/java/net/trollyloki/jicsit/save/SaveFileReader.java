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

    /**
     * Converts a .NET <a href="https://learn.microsoft.com/en-us/dotnet/api/system.datetime.ticks">DateTime.Ticks</a>
     * value to a Java {@link Instant}.
     *
     * @param ticks number of ticks
     * @return instant
     */
    public static Instant ticksToInstant(long ticks) {
        return TICKS_ORIGIN.plusMillis(ticks / 10000).plusNanos((ticks % 10000) * 100);
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

        if (headerVersion >= 11) stream.skipNBytes(4); // isPartitionedWorld
        if (headerVersion >= 13) stream.skipNBytes(4); // isCreativeModeEnabled

        byte[] checksum = null; // MD5 hash of all data after the header
        if (headerVersion >= 12) {
            checksum = stream.readNBytes(16);
        }

        int cheatFlag = headerVersion >= 13 ? readInt(stream) : 0; // not sure which header version added this

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
                cheatFlag != 0
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
            throw new SaveFormatException("Incorrect string terminator");
        }

        return string.substring(0, string.length() - 1);
    }

}
