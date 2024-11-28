package com.openclassrooms.SafetyNetAlert.service;


import com.openclassrooms.SafetyNetAlert.model.MedicalRecord;

import com.openclassrooms.SafetyNetAlert.model.Person;
import com.openclassrooms.SafetyNetAlert.util.JsonDataLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


/**
 * Service pour gérer les opérations relatives aux dossiers médicaux.
 * Fournit des fonctionnalités pour récupérer, ajouter, mettre à jour et supprimer des dossiers médicaux
 * et effectuer des opérations CRUD dessus.
 */
@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    @Autowired
    private final JsonDataLoader jsonDataLoader;

    /**
     * Récupère tous les dossiers médicaux disponibles.
     *
     * @return une liste de tous les dossiers médicaux {@link MedicalRecord}.
     */
    public List<MedicalRecord> getAllMedicalRecords() {
        return jsonDataLoader.getDataContainer().getMedicalRecords(); // Récupère la liste des dossiers médicaux
    }

    /**
     * Récupère un dossier médical en fonction du prénom et du nom.
     *
     * @param firstName le prénom de la personne.
     * @param lastName le nom de la personne.
     * @return le dossier médical correspondant à la personne {@link MedicalRecord}.
     * @throws RuntimeException si aucun dossier médical n'est trouvé pour le nom donné.
     */
    public MedicalRecord getMedicalRecordByName(String firstName, String lastName) {
        return jsonDataLoader.getDataContainer().getMedicalRecords().stream()
                .filter(record -> record.getFirstName().equals(firstName) && record.getLastName().equals(lastName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Medical record not found for: " + firstName + " " + lastName));
    }

    /**
     * Ajoute un nouveau dossier médical.
     *
     * @param medicalRecord le dossier médical à ajouter.
     * @return le dossier médical ajouté {@link MedicalRecord}.
     */
    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        jsonDataLoader.getDataContainer().getMedicalRecords().add(medicalRecord); // Ajoute un dossier médical
        jsonDataLoader.saveData();
        return medicalRecord;
    }

    /**
     * Met à jour un dossier médical existant en fonction du prénom et du nom.
     *
     * @param firstName le prénom de la personne.
     * @param lastName le nom de la personne.
     * @param updatedRecord le dossier médical contenant les mises à jour.
     * @return le dossier médical mis à jour {@link MedicalRecord}.
     * @throws RuntimeException si aucun dossier médical n'est trouvé pour le nom donné.
     */
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

    /**
     * Supprime un dossier médical en fonction du prénom et du nom.
     *
     * @param firstName le prénom de la personne.
     * @param lastName le nom de la personne.
     */
    public void deleteMedicalRecord(String firstName, String lastName) {
        List<MedicalRecord> medicalRecords = jsonDataLoader.getDataContainer().getMedicalRecords();
        medicalRecords.removeIf(record -> record.getFirstName().equals(firstName) && record.getLastName().equals(lastName));
        jsonDataLoader.saveData();
    }


}


