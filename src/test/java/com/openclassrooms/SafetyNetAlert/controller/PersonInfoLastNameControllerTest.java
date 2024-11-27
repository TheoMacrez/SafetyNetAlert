package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.service.*;

import com.openclassrooms.SafetyNetAlert.util.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "com.openclassrooms.safety-net-alert.dataFilePath=src/test/resources/testDataOriginal.json")
@AutoConfigureMockMvc
class PersonInfoLastNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PersonInfoLastNameService personInfoService;


    @Test
    void testGetPersonsByLastName_Success() throws Exception {
        // Mock the response from the service
        mockMvc.perform(get("/personInfolastName")
                        .param("lastName", "Boyd")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Boyd"));
    }

    @Test
    void testGetPersonsByLastName_NotFound() throws Exception {
        when(personInfoService.getPersonsByLastName("NonExistentLastName"))
                .thenThrow(new ResourceNotFoundException("Aucune personne trouvée avec le nom de famille : NonExistentLastName"));

        mockMvc.perform(get("/personInfolastName")
                        .param("lastName", "NonExistentLastName")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

