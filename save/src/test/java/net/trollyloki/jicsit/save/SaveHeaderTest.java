package net.trollyloki.jicsit.save;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SaveHeaderTest {

    private static final List<Arguments> mockSaveFileMapOptions = MockSaveFiles.VALUES.values().stream()
            .map(values -> arguments(values.info().header(), values.parsedMapOptions())).toList();

    @ParameterizedTest
    @FieldSource("mockSaveFileMapOptions")
    void parseMapOptions(SaveHeader header, Map<String, String> expected) {
        assertEquals(expected, header.parseMapOptions());
    }

}
