package net.trollyloki.jicsit.save;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SaveFileInfoTest {

    private static final List<Arguments> mockSaveFileModMetadata = MockSaveFiles.VALUES.values().stream()
            .map(values -> arguments(values.info(), values.modMetadataValid(), values.parsedModMetadata())).toList();

    @ParameterizedTest
    @FieldSource("mockSaveFileModMetadata")
    void parseModMetadata(SaveFileInfo info, boolean valid, ModMetadata expected) throws SaveFormatException {
        if (valid) {
            assertEquals(expected, info.parseModMetadata());
        } else {
            assertThrows(SaveFormatException.class, info::parseModMetadata);
        }
    }

}
