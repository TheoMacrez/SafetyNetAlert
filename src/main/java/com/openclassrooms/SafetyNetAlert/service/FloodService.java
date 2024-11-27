package com.openclassrooms.SafetyNetAlert.service;

import com.openclassrooms.SafetyNetAlert.dto.*;
import com.openclassrooms.SafetyNetAlert.model.*;
import com.openclassrooms.SafetyNetAlert.util.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class FloodService {

    @Autowired
    private JsonDataLoader jsonDataLoader;

    public FloodResponse getHouseholdsByStations(List<Integer> stationNumbers) {
        // 1. Récupère les adresses desservies par les casernes
        List<String> addresses = jsonDataLoader.getDataContainer().getFirestations().stream()
                .filter(firestation -> {
                    try {
                        return stationNumbers.contains(Integer.parseInt(firestation.getStation()));
                    } catch (NumberFormatException e) {
                        throw new DataFormatException("Le numéro de station dans les données JSON est invalide : " + firestation.getStation(), e);
                    }
                })
                .map(Firestation::getAddress)
                .toList();

        if (addresses.isEmpty()) {
            throw new ResourceNotFoundException("Aucune adresse trouvée pour les casernes : " + stationNumbers);
        }

        Map<String, List<FloodPersonInfo>> households = new HashMap<>();

        for (String address : addresses) {
            // Récupère les résidents vivant à cette adresse
            List<Person> residents = jsonDataLoader.getDataContainer().getPersons().stream()
                    .filter(person -> person.getAddress().equals(address))
                    .toList();

            if (!residents.isEmpty()) {
                List<FloodPersonInfo> residentDetails = residents.stream().map(person -> {
                    // Récupère le dossier médical de chaque résident
                    MedicalRecord record = jsonDataLoader.getDataContainer().getMedicalRecords().stream()
                            .filter(medicalRecord -> medicalRecord.getFirstName().equals(person.getFirstName())
                                    && medicalRecord.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Aucun dossier médical trouvé pour : " + person.getFirstName() + " " + person.getLastName()));

                    // Calcule l'âge à partir de la date de naissance
                    int age = CalculateAgeUtil.calculateAge(record.getBirthdate());

                    // Transforme les données en FloodPersonInfo
                    return new FloodPersonInfo(person.getFirstName()+" "+person.getLastName(), person.getPhone(), age,
                            record.getMedications(), record.getAllergies());
                }).toList();

                households.put(address, residentDetails);
            }
        }

        return new FloodResponse(households);
    }

}
