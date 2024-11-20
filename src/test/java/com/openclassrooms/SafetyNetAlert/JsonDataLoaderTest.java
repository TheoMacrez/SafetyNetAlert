package com.openclassrooms.SafetyNetAlert;

import com.openclassrooms.SafetyNetAlert.model.DataContainer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

class JsonDataLoaderTest {

    @Test
    void testLoadJsonData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/test/resources/testData.json");
        DataContainer dataContainer = objectMapper.readValue(file, DataContainer.class);

        assertNotNull(dataContainer.getPersons());
        assertEquals("John", dataContainer.getPersons().getFirst().getFirstName());
    }
}

