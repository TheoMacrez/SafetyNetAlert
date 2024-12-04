package com.openclassrooms.SafetyNetAlert.service;

import com.openclassrooms.SafetyNetAlert.dto.*;
import com.openclassrooms.SafetyNetAlert.model.*;
import com.openclassrooms.SafetyNetAlert.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhoneAlertServiceTest {

    @Mock
    private JsonDataLoader jsonDataLoader;

    private PhoneAlertService phoneAlertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        phoneAlertService = new PhoneAlertService(jsonDataLoader);
    }

    @Test
    void testGetPhoneNumbersByFirestation_ValidFirestation() {
        // Mock des données
        List<Firestation> mockFirestations = List.of(
                new Firestation("123 Elm Street","1"),
                new Firestation("456 Oak Avenue", "1")
        );

        List<Person> mockPersons = List.of(
                new Person("John", "Doe", "123 Elm Street", "City", "12345", "123-456-7890", "john.doe@example.com"),
                new Person("Jane", "Doe", "123 Elm Street", "City", "12345", "987-654-3210", "jane.doe@example.com"),
                new Person("Jack", "Smith", "456 Oak Avenue", "City", "12345", "123-456-7890", "jack.smith@example.com")
        );

        DataContainer mockData = new DataContainer();
        mockData.setFirestations(mockFirestations);
        mockData.setPersons(mockPersons);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Appeler la méthode
        PhoneAlertResponse response = phoneAlertService.getPhoneNumbersByFirestation(1);

        // Vérifications
        assertNotNull(response);
        assertEquals(2, response.getPhoneNumbers().size());
        assertTrue(response.getPhoneNumbers().contains("123-456-7890"));
        assertTrue(response.getPhoneNumbers().contains("987-654-3210"));
        verify(jsonDataLoader, times(2)).getDataContainer();
    }

    @Test
    void testGetPhoneNumbersByFirestation_NoResidents() {
        // Mock des données avec une caserne mais sans résidents
        List<Firestation> mockFirestations = List.of(new Firestation("123 Elm Street", "1"));
        List<Person> mockPersons = List.of();

        DataContainer mockData = new DataContainer();
        mockData.setFirestations(mockFirestations);
        mockData.setPersons(mockPersons);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Attente d'une exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> phoneAlertService.getPhoneNumbersByFirestation(1));

        assertEquals("Aucun résident trouvé pour la caserne : 1", exception.getMessage());
        verify(jsonDataLoader, times(2)).getDataContainer();
    }

    @Test
    void testGetPhoneNumbersByFirestation_InvalidFirestation() {
        // Mock des données sans la caserne spécifiée
        List<Firestation> mockFirestations = List.of(new Firestation("456 Oak Avenue", "2"));
        List<Person> mockPersons = List.of(
                new Person("John", "Doe", "123 Elm Street", "City", "12345", "123-456-7890", "john.doe@example.com")
        );

        DataContainer mockData = new DataContainer();
        mockData.setFirestations(mockFirestations);
        mockData.setPersons(mockPersons);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Attente d'une exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> phoneAlertService.getPhoneNumbersByFirestation(1));

        assertEquals("Aucune caserne trouvée pour le numéro : 1", exception.getMessage());
        verify(jsonDataLoader, times(1)).getDataContainer();
    }

    @Test
    void testGetPhoneNumbersByFirestation_DistinctPhoneNumbers() {
        // Mock des données avec des doublons de numéros de téléphone
        List<Firestation> mockFirestations = List.of(new Firestation("123 Elm Street", "1"));
        List<Person> mockPersons = List.of(
                new Person("John", "Doe", "123 Elm Street", "City", "12345", "123-456-7890", "john.doe@example.com"),
                new Person("Jane", "Doe", "123 Elm Street", "City", "12345", "123-456-7890", "jane.doe@example.com")
        );

        DataContainer mockData = new DataContainer();
        mockData.setFirestations(mockFirestations);
        mockData.setPersons(mockPersons);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Appeler la méthode
        PhoneAlertResponse response = phoneAlertService.getPhoneNumbersByFirestation(1);

        // Vérifications
        assertNotNull(response);
        assertEquals(1, response.getPhoneNumbers().size()); // Devrait y avoir qu'un seul numéro unique
        assertTrue(response.getPhoneNumbers().contains("123-456-7890"));
        verify(jsonDataLoader, times(2)).getDataContainer();
    }
}
