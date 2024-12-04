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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FloodServiceTest {

    @Mock
    private JsonDataLoader jsonDataLoader;

    private FloodService floodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        floodService = new FloodService(jsonDataLoader);

    }

    @Test
    void testGetHouseholdsByStations_ValidStationsWithResidents() {
        // Mock des données
        List<Firestation> mockFirestations = List.of(
                new Firestation("123 Elm Street", "1"),
                new Firestation("456 Oak Avenue", "2")
        );

        List<Person> mockPersons = List.of(
                new Person("John", "Doe", "123 Elm Street", "City", "12345", "123-456-7890", "john.doe@example.com"),
                new Person("Jane", "Smith", "456 Oak Avenue", "City", "12345", "123-456-7891", "jane.smith@example.com")
        );

        List<MedicalRecord> mockMedicalRecords = List.of(
                new MedicalRecord("John", "Doe", "01/01/1980", List.of("med1:100mg"), List.of("pollen")),
                new MedicalRecord("Jane", "Smith", "02/02/1975", List.of("med2:200mg"), List.of("dust"))
        );

        DataContainer mockData = new DataContainer();
        mockData.setFirestations(mockFirestations);
        mockData.setPersons(mockPersons);
        mockData.setMedicalRecords(mockMedicalRecords);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Appeler la méthode
        FloodResponse response = floodService.getHouseholdsByStations(List.of(1, 2));

        // Vérifications
        assertNotNull(response);
        assertEquals(2, response.getHouseholds().size());

        List<FloodPersonInfo> residentsElm = response.getHouseholds().get("123 Elm Street");
        assertNotNull(residentsElm);
        assertEquals(1, residentsElm.size());
        assertEquals("John Doe", residentsElm.getFirst().getName());
        assertEquals(44, residentsElm.getFirst().getAge());
        assertTrue(residentsElm.getFirst().getMedications().contains("med1:100mg"));

        List<FloodPersonInfo> residentsOak = response.getHouseholds().get("456 Oak Avenue");
        assertNotNull(residentsOak);
        assertEquals(1, residentsOak.size());
        assertEquals("Jane Smith", residentsOak.getFirst().getName());
        assertEquals(49, residentsOak.getFirst().getAge());
        assertTrue(residentsOak.getFirst().getMedications().contains("med2:200mg"));

        verify(jsonDataLoader, times(5)).getDataContainer();
    }

    @Test
    void testGetHouseholdsByStations_NoAddressesForStations() {
        // Mock des données sans adresse
        List<Firestation> mockFirestations = List.of();

        DataContainer mockData = new DataContainer();
        mockData.setFirestations(mockFirestations);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Attente d'une exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> floodService.getHouseholdsByStations(List.of(1, 2)));

        assertEquals("Aucune adresse trouvée pour les casernes : [1, 2]", exception.getMessage());
        verify(jsonDataLoader, times(1)).getDataContainer();
    }

    @Test
    void testGetHouseholdsByStations_ResidentWithoutMedicalRecord() {
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
                () -> floodService.getHouseholdsByStations(List.of(1)));

        assertEquals("Aucun dossier médical trouvé pour : John Doe", exception.getMessage());
        verify(jsonDataLoader, times(3)).getDataContainer();
    }

    @Test
    void testGetHouseholdsByStations_InvalidStationNumber() {
        // Mock des données avec un numéro de station invalide
        List<Firestation> mockFirestations = List.of(
                new Firestation("123 Elm Street", "INVALID")
        );

        DataContainer mockData = new DataContainer();
        mockData.setFirestations(mockFirestations);

        when(jsonDataLoader.getDataContainer()).thenReturn(mockData);

        // Attente d'une exception
        DataFormatException exception = assertThrows(DataFormatException.class,
                () -> floodService.getHouseholdsByStations(List.of(1)));

        assertTrue(exception.getMessage().contains("Le numéro de station dans les données JSON est invalide : INVALID"));
        verify(jsonDataLoader, times(1)).getDataContainer();
    }
}
