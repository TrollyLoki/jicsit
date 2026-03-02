package net.trollyloki.jicsit.save;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.FieldSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SaveFileReaderTest {

    @ParameterizedTest
    @CsvSource({
            "                        0, 0001-01-01T00:00:00Z",
            "   56_492_756_629_357_216, 0180-01-08T03:14:22.935721600Z",
            "  621_355_968_000_000_000, 1970-01-01T00:00:00Z",
            "  633_979_008_001_234_560, 2010-01-01T00:00:00.123456Z",
            "  636_893_006_971_320_000, 2019-03-27T16:24:57.132Z",
            "  639_017_177_287_160_000, 2025-12-19T05:08:48.716Z",
            "1_234_567_890_123_456_789, 3913-03-12T00:30:12.345678900Z",
            "3_155_378_975_999_999_999, 9999-12-31T23:59:59.999999900Z"
    })
    void ticksToInstant(long ticks, String expected) {
        assertEquals(expected, SaveFileReader.ticksToInstant(ticks).toString());
    }

    @ParameterizedTest
    @CsvSource({
            ".sav, ''",
            "test.sav, test",
            "sav.sav, sav",
            "sav.sav.sav, sav.sav",
            ".dot.sav, .dot",
            "dir/cool.sav, cool",
            "/root/dir/epic.thing.sav, epic.thing"
    })
    void saveNameOf(Path filePath, String expected) {
        assertEquals(expected, SaveFileReader.saveNameOf(filePath));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "a", ".", ".hidden", "some-file", "sav.txt", "file.sav.bak"})
    void saveNameOf_generic(String filename) {
        assertEquals(filename, SaveFileReader.saveNameOf(Path.of(filename)));
    }

    private static final List<Arguments> mockSaveFileInfo = MockSaveFiles.VALUES.entrySet().stream()
            .map(entry -> arguments(entry.getKey(), entry.getValue().info())).toList();

    @ParameterizedTest
    @FieldSource("mockSaveFileInfo")
    void readInfo(String name, SaveFileInfo expected) throws IOException {
        SaveFileInfo actual;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(name)) {
            actual = SaveFileReader.readInfo(SaveFileReader.saveNameOf(Path.of(name)), stream);
        }
        assertEquals(expected, actual);
    }

    private static final List<Arguments> invalidMockSaveFiles = MockSaveFiles.INVALID.entrySet().stream()
            .map(entry -> arguments(entry.getKey(), entry.getValue())).toList();

    @ParameterizedTest
    @FieldSource("invalidMockSaveFiles")
    void readInfo_invalid(String name, String expectedMessage) throws IOException {
        String actualMessage;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(name)) {
            IOException e = assertThrows(SaveFormatException.class, () -> SaveFileReader.readInfo("N/A", stream));
            actualMessage = e.getMessage();
        }
        assertEquals(expectedMessage, actualMessage);
    }

}
