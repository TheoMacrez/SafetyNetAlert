package com.openclassrooms.SafetyNetAlert.service;

import com.openclassrooms.SafetyNetAlert.dto.*;
import com.openclassrooms.SafetyNetAlert.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlert.model.Person;
import com.openclassrooms.SafetyNetAlert.util.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des informations des personnes par nom de famille.
 * Permet de récupérer les informations des personnes basées sur leur nom de famille.
 */
@Service
@RequiredArgsConstructor
public class PersonInfoLastNameService {

    @Autowired
    private final JsonDataLoader jsonDataLoader;

    /**
     * Récupère une liste de personnes basées sur le nom de famille.
     *
     * @param lastName le nom de famille à rechercher.
     * @return une liste de {@link PersonInfoLastNameResponse} contenant les informations des personnes trouvées.
     * @throws ResourceNotFoundException si aucune personne avec le nom de famille donné n'est trouvée.
     */
    public List<PersonInfoLastNameResponse> getPersonsByLastName(String lastName) {
        // Filtrer les personnes par nom de famille
        List<Person> persons = jsonDataLoader.getDataContainer().getPersons().stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .toList();

        if (persons.isEmpty()) {
            throw new ResourceNotFoundException("Aucune personne trouvée avec le nom de famille : " + lastName);
        }

        // Transformer les personnes en objets PersonInfoLastNameResponse
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

