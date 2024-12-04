package com.openclassrooms.SafetyNetAlert.service;

import com.fasterxml.jackson.databind.*;
import com.openclassrooms.SafetyNetAlert.dto.ChildAlertPersonInfo;
import com.openclassrooms.SafetyNetAlert.dto.ChildAlertResponse;
import com.openclassrooms.SafetyNetAlert.model.*;
import com.openclassrooms.SafetyNetAlert.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.*;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.*;

import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChildAlertServiceTest {

    @Mock
    private JsonDataLoader jsonDataLoader;

    private ChildAlertService childAlertService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        childAlertService = new ChildAlertService(jsonDataLoader);

    }

    @Test
    void testGetChildrenByAddress_WithChildrenAndHousehold() {

        // Mock des données
        List<Person> mockPersons = List.of(
                new Person("John", "Doe", "123 Elm Street", "City", "12345", "123-456-7890", "john.doe@example.com"),
                new Person("Jane", "Doe", "123 Elm Street", "City", "12345", "123-456-7891", "jane.doe@example.com")
        );

        List<MedicalRecord> mockMedicalRecords = List.of(
                new MedicalRecord("John", "Doe", "02/18/2012", List.of(), List.of()),
                new MedicalRecord("Jane", "Doe", "01/01/1980", List.of("med2:250mg"), List.of("dust"))
        );

        DataContainer mockData = new DataContainer();
        mockData.setPersons(mockPersons);
        mockData.setMedicalRecords(mockMedicalRecords);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Appeler la méthode
        ChildAlertResponse response = childAlertService.getChildrenByAddress("123 Elm Street");

        // Vérifications
        assertNotNull(response);
        assertEquals(1, response.getChildren().size());
        assertEquals("John", response.getChildren().getFirst().getFirstName());
        assertEquals(12, response.getChildren().getFirst().getAge()); // Calcul de l'âge
        assertEquals(1, response.getOtherHouseholdMembers().size());
        assertEquals("Jane Doe", response.getOtherHouseholdMembers().getFirst());

        // Vérification des interactions
        verify(jsonDataLoader, times(2)).getDataContainer();
    }

    @Test
    void testGetChildrenByAddress_NoPersonsAtAddress() {
        List<Person> mockPersons = List.of(
        );

        List<MedicalRecord> mockMedicalRecords = List.of(
        );

        DataContainer mockData = new DataContainer();
        mockData.setPersons(mockPersons);
        mockData.setMedicalRecords(mockMedicalRecords);

        // Mock sans personnes
        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Attente d'une exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> childAlertService.getChildrenByAddress("Unknown Address"));

        assertEquals("Aucune personne trouvée à cette adresse : Unknown Address", exception.getMessage());
        verify(jsonDataLoader, times(1)).getDataContainer();
    }

    @Test
    void testGetChildrenByAddress_NoChildrenAtAddress() {
        // Mock des données
        List<Person> mockPersons = List.of(
                new Person("Jane", "Doe", "123 Elm Street", "City", "12345", "123-456-7891", "jane.doe@example.com")
        );

        List<MedicalRecord> mockMedicalRecords = List.of(
                new MedicalRecord("Jane", "Doe", "01/01/1980", List.of("med2:250mg"), List.of("dust"))
        );

        DataContainer mockData = new DataContainer();
        mockData.setPersons(mockPersons);
        mockData.setMedicalRecords(mockMedicalRecords);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Exécuter la méthode
        ChildAlertResponse response = childAlertService.getChildrenByAddress("123 Elm Street");

        // Assertions
        assertNotNull(response);
        assertEquals(0, response.getChildren().size());
        assertEquals(1, response.getOtherHouseholdMembers().size());
        assertEquals("Jane Doe", response.getOtherHouseholdMembers().getFirst());

        verify(jsonDataLoader, times(2)).getDataContainer();
    }
}

