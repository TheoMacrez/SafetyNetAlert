package com.openclassrooms.SafetyNetAlert.service;

import com.openclassrooms.SafetyNetAlert.dto.ChildAlertResponse;
import com.openclassrooms.SafetyNetAlert.dto.ChildAlertPersonInfo;
import com.openclassrooms.SafetyNetAlert.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlert.model.Person;
import com.openclassrooms.SafetyNetAlert.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ChildAlertService {

    private final JsonDataLoader jsonDataLoader;

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

