package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.model.*;
import com.openclassrooms.SafetyNetAlert.service.*;
import com.openclassrooms.SafetyNetAlert.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "com.openclassrooms.safety-net-alert.dataFilePath=src/test/resources/testDataOriginal.json")
@AutoConfigureMockMvc
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MedicalRecordService medicalRecordService;

    /**
     * Test de l'ajout et update d'un nouveau dossier médical (POST).
     */
    @Test
    void testAddAndUpdateMedicalRecord() throws Exception {
        // Test Add
        String newMedicalRecord = """
            {
                "firstName":"Created", "lastName":"NewPerson", "birthdate":"01/01/2000", "medications":["aspirin:500mg"], "allergies":["pollen"]
            }
            """;

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMedicalRecord))
                .andExpect(status().isCreated());

        // Test Update
        String updatedMedicalRecord = """
            {
                "firstName":"Created", "lastName":"NewPerson", "birthdate":"03/06/1964", "medications":["pharmacol:5000mg"], "allergies":[]
            }
            """;

        mockMvc.perform(put("/medicalRecord/{firstName}/{lastName}", "Created", "NewPerson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMedicalRecord))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.birthdate").value("03/06/1964"))
                .andExpect(jsonPath("$.medications[0]").value("pharmacol:5000mg"));

        String firstNameToDelete = "Created";
        String lastNameToDelete = "NewPerson";

        mockMvc.perform(delete("/medicalRecord/{firstName}/{lastName}", firstNameToDelete,lastNameToDelete)
                .contentType(MediaType.APPLICATION_JSON));
    }



    /**
     * Test de suppression d'un dossier médical (DELETE).
     */
    @Test
    void testDeleteMedicalRecord() throws Exception {

        String newMedicalRecord = """
            {
                "firstName":"Created", "lastName":"NewPerson", "birthdate":"01/01/2000", "medications":["aspirin:500mg"], "allergies":["pollen"]
            }
            """;

        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newMedicalRecord));


        String firstNameToDelete = "Created";
        String lastNameToDelete = "NewPerson";

        mockMvc.perform(delete("/medicalRecord/{firstName}/{lastName}", firstNameToDelete,lastNameToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Vérifie que le statut est 204
    }


    /**
     * Test de suppression d'un dossier médical inexistant (DELETE).
     */
    @Test
    void testDeleteNonExistentMedicalRecord() throws Exception {
        String nonExistentFirstName = "Unknown Person First Name";
        String nonExistentLastName = "Unknown Person Last Name";

        mockMvc.perform(delete("/medicalRecord/{firstName}/{lastName}", nonExistentFirstName,nonExistentLastName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Vérifie que le statut est 404

    }


}
