package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.service.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "com.openclassrooms.safety-net-alert.dataFilePath=src/test/resources/testDataOriginal.json")
@AutoConfigureMockMvc
public class FireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FireService fireService;

    @Test
    void testGetResidentsByAddress_Success() throws Exception {
        mockMvc.perform(get("/fire")
                        .param("address", "1509 Culver St")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber").value("3"))
                .andExpect(jsonPath("$.residents[0].lastName").value("Boyd"))
                .andExpect(jsonPath("$.residents[0].phone").value("841-874-6512"))
                .andExpect(jsonPath("$.residents[0].age").value(40))
                .andExpect(jsonPath("$.residents[0].medications[0]").value("aznol:350mg"))
                .andExpect(jsonPath("$.residents[0].allergies[0]").value("nillacilan"));
    }

    @Test
    void testGetResidentsByAddress_AddressNotFound() throws Exception {
        mockMvc.perform(get("/fire")
                        .param("address", "Unknown Address")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Aucune caserne trouvée pour l'adresse : Unknown Address"));
    }

    @Test
    void testGetResidentsByAddress_NoResidents() throws Exception {
        mockMvc.perform(get("/fire")
                        .param("address", "Empty Street")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Aucun résident trouvé pour l'adresse : Empty Street"));
    }



}
