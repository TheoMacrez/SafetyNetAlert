package com.openclassrooms.SafetyNetAlert.util;

import com.openclassrooms.SafetyNetAlert.model.DataContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class JsonDataLoaderTest {

    private JsonDataLoader jsonDataLoader;

    @TempDir
    Path tempDir; // Répertoire temporaire unique pour chaque test

    @BeforeEach
    void setUp() throws IOException {
        // Chemin vers le fichier original dans resources
        Path originalFilePath = Path.of("src/test/resources/testDataOriginal.json");

        // Chemin vers le fichier temporaire dans le répertoire temporaire
        Path tempFilePath = tempDir.resolve("testData.json");

        // Copier le fichier original dans le répertoire temporaire
        Files.copy(originalFilePath, tempFilePath);

        // Initialiser JsonDataLoader avec le chemin du fichier temporaire
        jsonDataLoader = new JsonDataLoader(new ObjectMapper(), tempFilePath.toString());
    }

    @Test
    void testLoadData_ShouldLoadAllData() {
        // Vérifier que les données sont chargées correctement
        DataContainer dataContainer = jsonDataLoader.getDataContainer();

        assertThat(dataContainer).isNotNull();
        assertThat(dataContainer.getPersons()).isNotEmpty(); // Exemple d'assertion
    }

    @Test
    void testSaveData_ShouldPersistData() throws IOException {
        // Modifier les données
        jsonDataLoader.getDataContainer().getPersons().clear();

        // Sauvegarder les données
        jsonDataLoader.saveData();

        // Relire le fichier pour vérifier que les données ont été sauvegardées
        DataContainer reloadedData = new ObjectMapper().readValue(tempDir.resolve("testData.json").toFile(), DataContainer.class);

        assertThat(reloadedData.getPersons()).isEmpty();
    }
}

