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
class PersonInfoLastNameServiceTest {

    @Mock
    private JsonDataLoader jsonDataLoader;

    private PersonInfoLastNameService personInfoLastNameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personInfoLastNameService = new PersonInfoLastNameService(jsonDataLoader);

    }

    @Test
    void testGetPersonsByLastName_ValidLastName() {
        // Mock des données
        List<Person> mockPersons = List.of(
                new Person("John", "Doe", "123 Elm Street", "City", "12345", "123-456-7890", "john.doe@example.com"),
                new Person("Jane", "Doe", "456 Oak Avenue", "City", "12345", "123-456-7891", "jane.doe@example.com")
        );

        List<MedicalRecord> mockMedicalRecords = List.of(
                new MedicalRecord("John", "Doe", "01/01/1980", List.of("med1:100mg"), List.of("pollen")),
                new MedicalRecord("Jane", "Doe", "02/02/1975", List.of("med2:200mg"), List.of("dust"))
        );

        DataContainer mockData = new DataContainer();
        mockData.setPersons(mockPersons);
        mockData.setMedicalRecords(mockMedicalRecords);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Appeler la méthode
        List<PersonInfoLastNameResponse> responses = personInfoLastNameService.getPersonsByLastName("Doe");

        // Vérifications
        assertNotNull(responses);
        assertEquals(2, responses.size());

        PersonInfoLastNameResponse john = responses.stream()
                .filter(res -> res.getFirstName().equals("John"))
                .findFirst()
                .orElseThrow();
        assertEquals("Doe", john.getLastName());
        assertEquals(44, john.getAge());
        assertTrue(john.getMedications().contains("med1:100mg"));

        PersonInfoLastNameResponse jane = responses.stream()
                .filter(res -> res.getFirstName().equals("Jane"))
                .findFirst()
                .orElseThrow();
        assertEquals("Doe", jane.getLastName());
        assertEquals(49, jane.getAge());
        assertTrue(jane.getAllergies().contains("dust"));

        verify(jsonDataLoader, times(3)).getDataContainer();
    }

    @Test
    void testGetPersonsByLastName_NoMatch() {
        // Mock des données sans correspondances
        List<Person> mockPersons = List.of();
        DataContainer mockData = new DataContainer();
        mockData.setPersons(mockPersons);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Attente d'une exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> personInfoLastNameService.getPersonsByLastName("Doe"));

        assertEquals("Aucune personne trouvée avec le nom de famille : Doe", exception.getMessage());
        verify(jsonDataLoader, times(1)).getDataContainer();
    }

    @Test
    void testGetPersonsByLastName_NoMedicalRecord() {
        // Mock des données avec une personne sans dossier médical
        List<Person> mockPersons = List.of(
                new Person("John", "Doe", "123 Elm Street", "City", "12345", "123-456-7890", "john.doe@example.com")
        );

        List<MedicalRecord> mockMedicalRecords = List.of();

        DataContainer mockData = new DataContainer();
        mockData.setPersons(mockPersons);
        mockData.setMedicalRecords(mockMedicalRecords);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Attente d'une exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> personInfoLastNameService.getPersonsByLastName("Doe"));

        assertEquals("Aucun dossier médical trouvé pour : John Doe", exception.getMessage());
        verify(jsonDataLoader, times(2)).getDataContainer();
    }

    @Test
    void testGetPersonsByLastName_CaseInsensitiveSearch() {
        // Mock des données
        List<Person> mockPersons = List.of(
                new Person("John", "DOE", "123 Elm Street", "City", "12345", "123-456-7890", "john.doe@example.com")
        );

        List<MedicalRecord> mockMedicalRecords = List.of(
                new MedicalRecord("John", "DOE", "01/01/1980", List.of("med1:100mg"), List.of("pollen"))
        );

        DataContainer mockData = new DataContainer();
        mockData.setPersons(mockPersons);
        mockData.setMedicalRecords(mockMedicalRecords);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Appeler la méthode
        List<PersonInfoLastNameResponse> responses = personInfoLastNameService.getPersonsByLastName("doe");

        // Vérifications
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("John", responses.get(0).getFirstName());
        assertEquals("DOE", responses.get(0).getLastName());
        verify(jsonDataLoader, times(2)).getDataContainer();
    }
}
