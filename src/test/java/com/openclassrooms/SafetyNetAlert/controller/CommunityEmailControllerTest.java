package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.service.CommunityEmailService;
import com.openclassrooms.SafetyNetAlert.util.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "com.openclassrooms.safety-net-alert.dataFilePath=src/test/resources/testDataOriginal.json")
@AutoConfigureMockMvc
class CommunityEmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CommunityEmailService communityEmailService;

    @Test
    void testGetEmailsByCity_Success() throws Exception {

        mockMvc.perform(get("/communityEmail")
                        .param("city", "Culver")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("jaboyd@email.com"))
                .andExpect(jsonPath("$[1]").value("drk@email.com"));
    }

    @Test
    void testGetEmailsByCity_NotFound() throws Exception {
        when(communityEmailService.getEmailsByCity("UnknownCity"))
                .thenThrow(new ResourceNotFoundException("Aucun habitant trouvé pour la ville : UnknownCity"));

        mockMvc.perform(get("/communityEmail")
                        .param("city", "UnknownCity")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
