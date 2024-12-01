package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.model.*;
import com.openclassrooms.SafetyNetAlert.service.FirestationService;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;

import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(properties = "com.openclassrooms.safety-net-alert.dataFilePath=src/test/resources/testDataOriginal.json")
@AutoConfigureMockMvc
class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FirestationService firestationService;

    @Mock
    private FirestationController firestationController;


    /**
     * Test de l'ajout et update d'une nouvelle caserne (POST et PUT).
     */
    @Test
    void testAddAndUpdateFirestation() throws Exception {
        String newFirestation = """
                {
                    "address": "1304 Created St",
                    "station": 4
                }
                """;

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFirestation))
                .andExpect(status().isCreated()) // Vérifie que le statut est 201
                .andExpect(jsonPath("$.address").value("1304 Created St")) // Vérifie l'adresse
                .andExpect(jsonPath("$.station").value(4)); // Vérifie le numéro de station

        String updatedFirestation = """
                {
                    "address": "1304 Created St",
                    "station": 5
                }
                """;

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedFirestation))
                .andExpect(status().isOk()) // Vérifie que le statut est 200// Vérifie l'adresse
                .andExpect(jsonPath("$.station").value(5));

        String addressToDelete = "1304 Created St";

        mockMvc.perform(delete("/firestation/{address}", addressToDelete)
                .contentType(MediaType.APPLICATION_JSON));
    }



    /**
     * Test de suppression d'une caserne (DELETE).
     */
    @Test
    void testDeleteFirestation() throws Exception {

        String newFirestation = """
                {
                    "address": "1304 Created St",
                    "station": 4
                }
                """;
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newFirestation));

        String addressToDelete = "1304 Created St";

        mockMvc.perform(delete("/firestation/{address}", addressToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Vérifie que le statut est 204
    }

    /**
     * Test de suppression d'une caserne inexistante (DELETE).
     */
    @Test
    void testDeleteNonExistentFirestation() throws Exception {
        String nonExistentAddress = "Unknown Address";

        mockMvc.perform(delete("/firestation/{address}", nonExistentAddress)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Vérifie que le statut est 404

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

}

