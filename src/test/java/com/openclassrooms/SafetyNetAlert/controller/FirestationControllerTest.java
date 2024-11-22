package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.dto.StationCoveragePersonInfo;
import com.openclassrooms.SafetyNetAlert.service.FirestationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FirestationController.class)
class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FirestationService firestationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPersonsByFirestationNumber() throws Exception {
        // Données de test
        int stationNumber = 1;

        // Test de l'URL
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", String.valueOf(stationNumber))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Vérifie que le statut est 200
                .andExpect(jsonPath("$.persons[0].firstName").value("John")) // Vérifie le prénom du premier résultat
                .andExpect(jsonPath("$.persons[1].lastName").value("Doe")) // Vérifie le nom du second résultat
                .andExpect(jsonPath("$.adults").value(1)) // Vérifie le nombre d'adultes
                .andExpect(jsonPath("$.children").value(1)); // Vérifie le nombre d'enfants
    }
}

