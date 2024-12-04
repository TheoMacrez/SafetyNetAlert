package com.openclassrooms.SafetyNetAlert.service;

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
class CommunityEmailServiceTest {

    @Mock
    private JsonDataLoader jsonDataLoader;

    private CommunityEmailService communityEmailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        communityEmailService = new CommunityEmailService(jsonDataLoader);

    }

    @Test
    void testGetEmailsByCity_WithValidCity() {
        // Mock des données
        List<Person> mockPersons = List.of(
                new Person("John", "Doe", "123 Elm Street", "CityA", "12345", "123-456-7890", "john.doe@example.com"),
                new Person("Jane", "Smith", "456 Oak Street", "CityA", "12345", "123-456-7891", "jane.smith@example.com")
        );

        DataContainer mockData = new DataContainer();
        mockData.setPersons(mockPersons);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Appeler la méthode
        List<String> emails = communityEmailService.getEmailsByCity("CityA");

        // Vérifications
        assertNotNull(emails);
        assertEquals(2, emails.size());
        assertTrue(emails.contains("john.doe@example.com"));
        assertTrue(emails.contains("jane.smith@example.com"));

        verify(jsonDataLoader, times(1)).getDataContainer();
    }

    @Test
    void testGetEmailsByCity_WithNoResidents() {
        // Mock des données vides
        List<Person> mockPersons = List.of();

        DataContainer mockData = new DataContainer();
        mockData.setPersons(mockPersons);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Attente d'une exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> communityEmailService.getEmailsByCity("UnknownCity"));

        assertEquals("Aucun habitant trouvé pour la ville : UnknownCity", exception.getMessage());
        verify(jsonDataLoader, times(1)).getDataContainer();
    }

    @Test
    void testGetEmailsByCity_WithDuplicateEmails() {
        // Mock des données avec doublons
        List<Person> mockPersons = List.of(
                new Person("John", "Doe", "123 Elm Street", "CityB", "12345", "123-456-7890", "john.doe@example.com"),
                new Person("Jane", "Doe", "456 Oak Street", "CityB", "12345", "123-456-7891", "john.doe@example.com") // Même email
        );

        DataContainer mockData = new DataContainer();
        mockData.setPersons(mockPersons);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Appeler la méthode
        List<String> emails = communityEmailService.getEmailsByCity("CityB");

        // Vérifications
        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertTrue(emails.contains("john.doe@example.com"));

        verify(jsonDataLoader, times(1)).getDataContainer();
    }
}

