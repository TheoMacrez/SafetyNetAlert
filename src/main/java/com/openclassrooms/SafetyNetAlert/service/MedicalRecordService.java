package com.openclassrooms.SafetyNetAlert.service;


import com.openclassrooms.SafetyNetAlert.model.MedicalRecord;

import com.openclassrooms.SafetyNetAlert.model.Person;
import com.openclassrooms.SafetyNetAlert.util.JsonDataLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    @Autowired
    private final JsonDataLoader jsonDataLoader;

    public List<MedicalRecord> getAllMedicalRecords() {
        return jsonDataLoader.getDataContainer().getMedicalRecords(); // Récupère la liste des dossiers médicaux
    }

    public MedicalRecord getMedicalRecordByName(String firstName, String lastName) {
        return jsonDataLoader.getDataContainer().getMedicalRecords().stream()
                .filter(record -> record.getFirstName().equals(firstName) && record.getLastName().equals(lastName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Medical record not found for: " + firstName + " " + lastName));
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        jsonDataLoader.getDataContainer().getMedicalRecords().add(medicalRecord); // Ajoute un dossier médical
        jsonDataLoader.saveData();
        return medicalRecord;
    }

    public MedicalRecord updateMedicalRecord(String firstName, String lastName,MedicalRecord updatedRecord) {

        List<MedicalRecord> medicalRecords = jsonDataLoader.getDataContainer().getMedicalRecords();

        MedicalRecord medicalRecord =medicalRecords.stream()
                .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("MedicalRecords not found"));

        medicalRecord.setBirthdate(updatedRecord.getBirthdate());
        medicalRecord.setMedications(updatedRecord.getMedications());
        medicalRecord.setAllergies(updatedRecord.getAllergies());
        jsonDataLoader.saveData();
        return medicalRecord;
    }

    public void deleteMedicalRecord(String firstName, String lastName) {
        List<MedicalRecord> medicalRecords = jsonDataLoader.getDataContainer().getMedicalRecords();
        medicalRecords.removeIf(record -> record.getFirstName().equals(firstName) && record.getLastName().equals(lastName));
        jsonDataLoader.saveData();
    }


}


