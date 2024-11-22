package com.openclassrooms.SafetyNetAlert.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlert.model.Firestation;
import com.openclassrooms.SafetyNetAlert.util.JsonDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class FirestationServiceTest {

    @Autowired
    private JsonDataLoader jsonDataLoader;

    private FirestationService firestationService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {


        // Chemin vers le fichier original dans resources
        Path originalFilePath = Path.of("src/test/resources/testDataOriginal.json");

        // Chemin vers le fichier temporaire dans le répertoire temporaire
        Path tempFilePath = tempDir.resolve("testData.json");

        // Copier le fichier original dans le répertoire temporaire
        Files.copy(originalFilePath, tempFilePath);

        // Initialiser JsonDataLoader avec le chemin du fichier temporaire
        JsonDataLoader jsonDataLoader = new JsonDataLoader(new ObjectMapper(), tempFilePath.toString());

        firestationService = new FirestationService(jsonDataLoader);
    }

    @Test
    void getAllFirestations_ShouldReturnAllFirestations() {
        List<Firestation> firestations = firestationService.getAllFirestations();

        assertThat(firestations).isNotEmpty();
        assertThat(firestations.size()).isEqualTo(13); // Exemple : supposez 10 casernes dans testData.json
    }

    @Test
    void addFirestation_ShouldAddNewFirestation() {
        Firestation newFirestation = new Firestation("123 New Ave", "5");
        firestationService.addFirestation(newFirestation);

        List<Firestation> firestations = firestationService.getAllFirestations();
        assertThat(firestations).contains(newFirestation);
        assertThat(firestations.size()).isEqualTo(14);
    }

    @Test
    void updateFirestation_ShouldUpdateExistingFirestation() {
        Firestation newFirestation = new Firestation("123 New Ave", "5");
        firestationService.addFirestation(newFirestation);

        newFirestation.setStation("6");

        firestationService.updateFirestation(newFirestation);

        Firestation firestationTest = firestationService.getFirestationByAddress("123 New Ave");

        assertThat(firestationTest.getStation()).isEqualTo("6");
    }

    @Test
    void deleteFirestation_ShouldRemoveFirestation() {


        // Vérifier si la firestation est deja présente
        try {
            Firestation existingFireStation = firestationService.getFirestationByAddress("123 New Ave");

            assertThat(existingFireStation).isNotNull();
        } catch (RuntimeException e) {

            Firestation newFirestation = new Firestation("123 New Ave", "5");
            firestationService.addFirestation(newFirestation);
        }

        firestationService.deleteFirestation("123 New Ave");

        List<Firestation> firestations = firestationService.getAllFirestations();
        assertThat(firestations.stream().anyMatch(f -> f.getAddress().equals("123 New Ave"))).isFalse();
    }
}
