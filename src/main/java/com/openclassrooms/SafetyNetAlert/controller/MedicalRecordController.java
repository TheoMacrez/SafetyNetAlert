package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlert.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller pour gérer les opérations CRUD sur les dossiers médicaux (MedicalRecord).
 */
@RestController
@RequestMapping("/medicalrecord")
@RequiredArgsConstructor
public class MedicalRecordController {

    @Autowired
    private final MedicalRecordService medicalRecordService;

    /**
     * Ajoute un nouveau dossier médical.
     *
     * @param medicalRecord L'objet MedicalRecord à ajouter.
     * @return Le dossier médical créé avec un statut HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        MedicalRecord createdMedicalRecord = medicalRecordService.addMedicalRecord(medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMedicalRecord);
    }

    /**
     * Met à jour un dossier médical existant en fonction du prénom et du nom de la personne.
     *
     * @param firstName     Le prénom de la personne.
     * @param lastName      Le nom de la personne.
     * @param medicalRecord L'objet MedicalRecord contenant les nouvelles informations.
     * @return Le dossier médical mis à jour.
     */
    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable String firstName,
                                                             @PathVariable String lastName,
                                                             @RequestBody MedicalRecord medicalRecord) {
        MedicalRecord updatedMedicalRecord = medicalRecordService.updateMedicalRecord(firstName, lastName, medicalRecord);
        return ResponseEntity.ok(updatedMedicalRecord);
    }

    /**
     * Supprime un dossier médical en fonction du prénom et du nom de la personne.
     *
     * @param firstName Le prénom de la personne.
     * @param lastName  Le nom de la personne.
     * @return Une réponse vide avec un statut HTTP 204 (No Content).
     */
    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String firstName,
                                                    @PathVariable String lastName) {
        medicalRecordService.deleteMedicalRecord(firstName, lastName);
        return ResponseEntity.noContent().build();
    }
}


