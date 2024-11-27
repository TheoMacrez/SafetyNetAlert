package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.service.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "com.openclassrooms.safety-net-alert.dataFilePath=src/test/resources/testDataOriginal.json")
@AutoConfigureMockMvc
public class FloodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FloodService floodService;

    @Test
    void testGetHouseholdsByStations_Success() throws Exception {
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1,2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.households['951 LoneTree Rd']").isArray())
                .andExpect(jsonPath("$.households['951 LoneTree Rd'][0].name").value("Eric Cadigan"))
                .andExpect(jsonPath("$.households['951 LoneTree Rd'][0].age").value(79));
    }


    @Test
    void testGetHouseholdsByStations_NoFirestationFound() throws Exception {
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "99,100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void testGetHouseholdsByStations_MissingStationsParam() throws Exception {
        mockMvc.perform(get("/flood/stations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}
