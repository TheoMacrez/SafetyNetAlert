package com.openclassrooms.SafetyNetAlert.service;

import com.openclassrooms.SafetyNetAlert.dto.*;
import com.openclassrooms.SafetyNetAlert.model.*;
import com.openclassrooms.SafetyNetAlert.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class FireService {

    @Autowired
    private JsonDataLoader jsonDataLoader;

    public FireResponse getResidentsByAddress(String address) {
        // Trouve la caserne de pompiers correspondant à l'adresse
        String stationNumber = jsonDataLoader.getDataContainer().getFirestations().stream()
                .filter(firestation -> firestation.getAddress().equals(address))
                .map(Firestation::getStation)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Aucune caserne trouvée pour l'adresse : " + address));

        // Récupère les personnes vivant à l'adresse
        List<Person> persons = jsonDataLoader.getDataContainer().getPersons().stream()
                .filter(person -> person.getAddress().equals(address))
                .toList();

        if (persons.isEmpty()) {
            throw new ResourceNotFoundException("Aucun résident trouvé pour l'adresse : " + address);
        }

        // Transforme les personnes en objets FireResidentInfo
        List<FirePersonInfo> residents = persons.stream().map(person -> {
            MedicalRecord record = jsonDataLoader.getDataContainer().getMedicalRecords().stream()
                    .filter(medicalRecord -> medicalRecord.getFirstName().equals(person.getFirstName())
                            && medicalRecord.getLastName().equals(person.getLastName()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Aucun dossier médical trouvé pour : " + person.getFirstName() + " " + person.getLastName()));

            int age = CalculateAgeUtil.calculateAge(record.getBirthdate());
            return new FirePersonInfo(person.getLastName(), person.getPhone(), age,
                    record.getMedications(), record.getAllergies());
        }).toList();

        // Crée et retourne la réponse
        return new FireResponse(stationNumber,residents);
    }
}

