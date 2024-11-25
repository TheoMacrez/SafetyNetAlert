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
public class ChildAlertControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ChildAlertService childAlertService;

    @Test
    void testGetChildrenByAddress_WithChildren() throws Exception {
        mockMvc.perform(get("/childAlert")
                        .param("address", "947 E. Rose Dr")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children").isArray())
                .andExpect(jsonPath("$.children[0].firstName").value("Kendrik"))
                .andExpect(jsonPath("$.children[0].age").value(10))
                .andExpect(jsonPath("$.otherHouseholdMembers").isArray());
    }

    @Test
    void testGetChildrenByAddress_NoChildren() throws Exception {
        mockMvc.perform(get("/childAlert")
                        .param("address", "908 73rd St")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children").isEmpty())
                .andExpect(jsonPath("$.otherHouseholdMembers").isArray());
    }

    @Test
    void testGetChildrenByAddress_AddressNotFound() throws Exception {
        mockMvc.perform(get("/childAlert")
                        .param("address", "Unknown Address")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Aucune personne trouvée à cette adresse : Unknown Address"));
    }



}
