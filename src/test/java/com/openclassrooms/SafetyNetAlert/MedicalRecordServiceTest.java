package com.openclassrooms.SafetyNetAlert;


import com.openclassrooms.SafetyNetAlert.model.Firestation;
import com.openclassrooms.SafetyNetAlert.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlert.service.MedicalRecordService;
import com.openclassrooms.SafetyNetAlert.util.JsonDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class MedicalRecordServiceTest {

    @Autowired
    private JsonDataLoader jsonDataLoader;

    private MedicalRecordService medicalRecordService;

    @BeforeEach
    void setUp() throws IOException {
        // Préparer une copie fraîche de testData.json avant chaque test
        File source = new File("src/test/resources/testDataOriginal.json");
        File destination = new File("src/test/resources/testData.json");
        FileSystemUtils.copyRecursively(source, destination);

        jsonDataLoader.loadDataFromFile("src/test/resources/testData.json");
        medicalRecordService = new MedicalRecordService(jsonDataLoader);
    }

    @Test
    void getAllMedicalRecords_ShouldReturnAllRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();

        assertThat(medicalRecords).isNotEmpty();
        assertThat(medicalRecords.size()).isEqualTo(23); // Exemple : supposez 23 fiches dans testData.json
    }

    @Test
    void addMedicalRecord_ShouldAddNewRecord() {
        MedicalRecord newRecord = new MedicalRecord("John", "Doe", "01/01/1990", List.of("med1"), List.of("allergy1"));
        medicalRecordService.addMedicalRecord(newRecord);

        List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();
        assertThat(medicalRecords).contains(newRecord);
        assertThat(medicalRecords.size()).isEqualTo(24);
    }

    @Test
    void deleteMedicalRecord_ShouldRemoveRecord() {

        try {
            MedicalRecord existingMedicalRecord = medicalRecordService.getMedicalRecordByName("John","Doe");

            assertThat(existingMedicalRecord).isNotNull();
        } catch (RuntimeException e) {

            MedicalRecord newMedicalRecord = new MedicalRecord("John", "Doe", "01/01/1990", List.of("med1"), List.of("allergy1"));
            medicalRecordService.addMedicalRecord(newMedicalRecord);
        }

        medicalRecordService.deleteMedicalRecord("John", "Doe");

        List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();
        assertThat(medicalRecords.stream().anyMatch(m -> m.getFirstName().equals("John") && m.getLastName().equals("Doe"))).isFalse();
    }
}

