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
class FireServiceTest {

    @Mock
    private JsonDataLoader jsonDataLoader;

    private FireService fireService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fireService = new FireService(jsonDataLoader);

    }

    @Test
    void testGetResidentsByAddress_ValidAddressWithResidents() {
        // Mock des données
        List<Firestation> mockFirestations = List.of(
                new Firestation("123 Elm Street", "1")
        );

        List<Person> mockPersons = List.of(
                new Person("John", "Doe", "123 Elm Street", "City", "12345", "123-456-7890", "john.doe@example.com")
        );

        List<MedicalRecord> mockMedicalRecords = List.of(
                new MedicalRecord("John", "Doe", "01/01/1980", List.of("med1:100mg"), List.of("pollen"))
        );

        DataContainer mockData = new DataContainer();
        mockData.setFirestations(mockFirestations);
        mockData.setPersons(mockPersons);
        mockData.setMedicalRecords(mockMedicalRecords);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Appeler la méthode
        FireResponse response = fireService.getResidentsByAddress("123 Elm Street");

        // Vérifications
        assertNotNull(response);
        assertEquals("1", response.getStationNumber());
        assertEquals(1, response.getResidents().size());

        FirePersonInfo resident = response.getResidents().getFirst();
        assertEquals("Doe", resident.getLastName());
        assertEquals("123-456-7890", resident.getPhone());
        assertEquals(44, resident.getAge()); // L'âge calculé doit être correct
        assertTrue(resident.getMedications().contains("med1:100mg"));
        assertTrue(resident.getAllergies().contains("pollen"));

        verify(jsonDataLoader, times(3)).getDataContainer();
    }

    @Test
    void testGetResidentsByAddress_NoFirestationForAddress() {
        // Mock des données sans caserne pour l'adresse
        List<Firestation> mockFirestations = List.of();

        DataContainer mockData = new DataContainer();
        mockData.setFirestations(mockFirestations);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Attente d'une exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> fireService.getResidentsByAddress("Unknown Address"));

        assertEquals("Aucune caserne trouvée pour l'adresse : Unknown Address", exception.getMessage());
        verify(jsonDataLoader, times(1)).getDataContainer();
    }

    @Test
    void testGetResidentsByAddress_NoResidentsForAddress() {
        // Mock des données avec une caserne mais sans résidents
        List<Firestation> mockFirestations = List.of(
                new Firestation("123 Elm Street", "1")
        );

        List<Person> mockPersons = List.of();

        DataContainer mockData = new DataContainer();
        mockData.setFirestations(mockFirestations);
        mockData.setPersons(mockPersons);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Attente d'une exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> fireService.getResidentsByAddress("123 Elm Street"));

        assertEquals("Aucun résident trouvé pour l'adresse : 123 Elm Street", exception.getMessage());
        verify(jsonDataLoader, times(2)).getDataContainer();
    }

    @Test
    void testGetResidentsByAddress_ResidentWithoutMedicalRecord() {
        // Mock des données avec un résident sans dossier médical
        List<Firestation> mockFirestations = List.of(
                new Firestation("123 Elm Street", "1")
        );

        List<Person> mockPersons = List.of(
                new Person("John", "Doe", "123 Elm Street", "City", "12345", "123-456-7890", "john.doe@example.com")
        );

        List<MedicalRecord> mockMedicalRecords = List.of();

        DataContainer mockData = new DataContainer();
        mockData.setFirestations(mockFirestations);
        mockData.setPersons(mockPersons);
        mockData.setMedicalRecords(mockMedicalRecords);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Attente d'une exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> fireService.getResidentsByAddress("123 Elm Street"));

        assertEquals("Aucun dossier médical trouvé pour : John Doe", exception.getMessage());
        verify(jsonDataLoader, times(3)).getDataContainer();
    }
}
