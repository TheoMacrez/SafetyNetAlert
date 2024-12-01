package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.service.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "com.openclassrooms.safety-net-alert.dataFilePath=src/test/resources/testDataOriginal.json")
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    /**
     * Test de l'ajout et update d'une nouvelle personne (POST et PUT).
     */
    @Test
    void testAddAndUpdatePerson() throws Exception {
        String newPerson = """
            {
                "firstName":"Created", "lastName":"NewPerson","address":"888 Tested St", "city":"TestCity", "zip":"12345", "phone":"123-456-7890", "email":"createdperson@email.com"
            }
            """;

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPerson))
                .andExpect(status().isCreated()) // Vérifie que le statut est 201
                .andExpect(jsonPath("$.firstName").value("Created")) // Vérifie l'adresse
                .andExpect(jsonPath("$.lastName").value("NewPerson")); // Vérifie le numéro de station

        String updatedPerson = """
            {
                "firstName":"Created", "lastName":"NewPerson", "address":"888 Updated St", "city":"UpdatedCity", "zip":"12345", "phone":"123-456-7890", "email":"createdperson@email.com"
            }
            """;

        mockMvc.perform(put("/person/{firstName}/{lastName}", "Created", "NewPerson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPerson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("888 Updated St"))
                .andExpect(jsonPath("$.city").value("UpdatedCity"));

        String firstNameToDelete = "Created";
        String lastNameToDelete = "NewPerson";

        mockMvc.perform(delete("/person/{firstName}/{lastName}", firstNameToDelete,lastNameToDelete)
                .contentType(MediaType.APPLICATION_JSON));
    }



    /**
     * Test de suppression d'une personne (DELETE).
     */
    @Test
    void testDeletePerson() throws Exception {
        String newPerson = """
            {
                "firstName":"Created", "lastName":"NewPerson","address":"888 Tested St", "city":"TestCity", "zip":"12345", "phone":"123-456-7890", "email":"createdperson@email.com"
            }
            """;

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newPerson));

        String firstNameToDelete = "Created";
        String lastNameToDelete = "NewPerson";

        mockMvc.perform(delete("/person/{firstName}/{lastName}", firstNameToDelete,lastNameToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Vérifie que le statut est 204 // Vérifie que le statut est 204
    }

    /**
     * Test de suppression d'une personne inexistante (DELETE).
     */
    @Test
    void testDeleteNonExistentPerson() throws Exception {
        String nonExistentFirstName = "Unknown Person First Name";
        String nonExistentLastName = "Unknown Person Last Name";

        mockMvc.perform(delete("/person/{firstName}/{lastName}", nonExistentFirstName,nonExistentLastName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Vérifie que le statut est 404
    }
}
