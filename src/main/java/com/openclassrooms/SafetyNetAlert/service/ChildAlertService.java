package com.openclassrooms.SafetyNetAlert.service;

import com.openclassrooms.SafetyNetAlert.dto.ChildAlertResponse;
import com.openclassrooms.SafetyNetAlert.dto.ChildAlertPersonInfo;
import com.openclassrooms.SafetyNetAlert.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlert.model.Person;
import com.openclassrooms.SafetyNetAlert.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service pour gérer les alertes enfants.
 * Il permet de récupérer les informations des enfants vivant à une adresse donnée,
 * ainsi que les autres membres du foyer.
 */
@Service
@RequiredArgsConstructor
public class ChildAlertService {

    @Autowired
    private final JsonDataLoader jsonDataLoader;

    /**
     * Récupère les informations des enfants et des autres membres du foyer vivant à une adresse donnée.
     *
     * @param address l'adresse à analyser.
     * @return une réponse contenant les informations des enfants et des membres du foyer.
     * @throws ResourceNotFoundException si aucune personne n'est trouvée à l'adresse spécifiée.
     */
    public ChildAlertResponse getChildrenByAddress(String address) {
        // Récupère toutes les personnes vivant à l'adresse
        List<Person> personsAtAddress = jsonDataLoader.getDataContainer().getPersons().stream()
                .filter(person -> person.getAddress().equals(address))
                .toList();

        if (personsAtAddress.isEmpty()) {
            throw new ResourceNotFoundException("Aucune personne trouvée à cette adresse : " + address);
        }

        // Récupère les dossiers médicaux des personnes
        List<MedicalRecord> medicalRecords = jsonDataLoader.getDataContainer().getMedicalRecords();

        // Liste des enfants
        List<ChildAlertPersonInfo> children = personsAtAddress.stream()
                .filter(person -> {
                    MedicalRecord record = medicalRecords.stream()
                            .filter(medicalRecord -> medicalRecord.getFirstName().equals(person.getFirstName())
                                    && medicalRecord.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElse(null);

                    return record != null && CalculateAgeUtil.isUnder18(record.getBirthdate());
                })
                .map(person -> {
                    MedicalRecord record = medicalRecords.stream()
                            .filter(medicalRecord -> medicalRecord.getFirstName().equals(person.getFirstName())
                                    && medicalRecord.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElseThrow();

                    int age = CalculateAgeUtil.calculateAge(record.getBirthdate());
                    return new ChildAlertPersonInfo(person.getFirstName(), person.getLastName(), age);
                })
                .toList();



        // Liste des autres membres du foyer
        List<String> otherHouseholdMembers = personsAtAddress.stream()
                .filter(person -> children.stream()
                        .noneMatch(child -> child.getFirstName().equals(person.getFirstName())
                                && child.getLastName().equals(person.getLastName())))
                .map(person -> person.getFirstName() + " " + person.getLastName())
                .toList();

        return new ChildAlertResponse(children, otherHouseholdMembers);
    }
}

