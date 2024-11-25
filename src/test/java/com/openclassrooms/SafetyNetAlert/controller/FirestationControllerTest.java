package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.dto.StationCoveragePersonInfo;
import com.openclassrooms.SafetyNetAlert.service.FirestationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(FirestationController.class)
@SpringBootTest(properties = "com.openclassrooms.safety-net-alert.dataFilePath=src/test/resources/testDataOriginal.json")
@AutoConfigureMockMvc
class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FirestationService firestationService;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void testGetPersonsByFirestationNumber() throws Exception {
        // Données de test
        int stationNumber = 1;

        // Test de l'URL
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", String.valueOf(stationNumber))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Vérifie que le statut est 200
                .andExpect(jsonPath("$.persons[0].firstName").value("Peter")) // Vérifie le prénom du premier résultat
                .andExpect(jsonPath("$.persons[1].lastName").value("Walker")) // Vérifie le nom du second résultat
                .andExpect(jsonPath("$.numberOfAdults").value(5)) // Vérifie le nombre d'adultes
                .andExpect(jsonPath("$.numberOfChildren").value(1)); // Vérifie le nombre d'enfants
    }


    @Test
    void testGetPersonsByFirestationNumber_InvalidStationNumber() throws Exception {
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "-1") // Numéro de station invalide
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Vérifie que le statut est 400
                .andExpect(content().string("Le numéro de station doit être un entier positif.")); // Message d'erreur attendu
    }

    @Test
    void testGetPersonsByFirestationNumber_NoAddressesFound() throws Exception {
        int stationNumber = 999; // Station inexistante

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", String.valueOf(stationNumber))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // Vérifie que le statut est 404
                .andExpect(content().string("Aucune adresse trouvée pour la station numéro : " + stationNumber));
    }


//    @Test
//    void testUnexpectedException() throws Exception {
//        // Simule une exception inattendue
//        when(firestationService.getPersonsCoveredByStation(anyInt())).thenThrow(new RuntimeException("Erreur inattendue"));
//
//        mockMvc.perform(get("/firestation")
//                        .param("stationNumber", "1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError()) // Vérifie que le statut est 500
//                .andExpect(content().string("Erreur inattendue"));
//    }
}

