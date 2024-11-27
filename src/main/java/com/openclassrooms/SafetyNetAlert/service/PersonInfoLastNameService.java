package com.openclassrooms.SafetyNetAlert.service;

import com.openclassrooms.SafetyNetAlert.dto.*;
import com.openclassrooms.SafetyNetAlert.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlert.model.Person;
import com.openclassrooms.SafetyNetAlert.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonInfoLastNameService {

    @Autowired
    private JsonDataLoader jsonDataLoader;

    public List<PersonInfoLastNameResponse> getPersonsByLastName(String lastName) {
        // Filtrer les personnes par nom de famille
        List<Person> persons = jsonDataLoader.getDataContainer().getPersons().stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .toList();

        if (persons.isEmpty()) {
            throw new ResourceNotFoundException("Aucune personne trouvée avec le nom de famille : " + lastName);
        }

        // Transformer les personnes en objets PersonInfoResponse
        return persons.stream().map(person -> {
            MedicalRecord record = jsonDataLoader.getDataContainer().getMedicalRecords().stream()
                    .filter(medicalRecord -> medicalRecord.getFirstName().equals(person.getFirstName())
                            && medicalRecord.getLastName().equals(person.getLastName()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Aucun dossier médical trouvé pour : " + person.getFirstName() + " " + person.getLastName()));

            int age = CalculateAgeUtil.calculateAge(record.getBirthdate());

            return new PersonInfoLastNameResponse(
                    person.getFirstName(),
                    person.getLastName(),
                    person.getAddress(),
                    age,
                    person.getEmail(),
                    record.getMedications(),
                    record.getAllergies()
            );
        }).toList();
    }
}

