package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.service.*;
import com.openclassrooms.SafetyNetAlert.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "com.openclassrooms.safety-net-alert.dataFilePath=src/test/resources/testDataOriginal.json")
@AutoConfigureMockMvc
public class PhoneAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PhoneAlertService phoneAlertService;


    @Test
    void testGetPhoneNumbersByFirestation_Success() throws Exception {
        // Simule des données
        int firestationNumber = 1;

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", String.valueOf(firestationNumber))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumbers[0]").value("841-874-6512"))
                .andExpect(jsonPath("$.phoneNumbers[1]").value("841-874-8547"));
    }

    @Test
    void testGetPhoneNumbersByFirestation_NotFound() throws Exception {
        // Cas où la caserne n'existe pas
        int firestationNumber = 99;

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", String.valueOf(firestationNumber))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
