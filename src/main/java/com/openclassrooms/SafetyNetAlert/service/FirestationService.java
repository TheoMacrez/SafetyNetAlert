package com.openclassrooms.SafetyNetAlert.service;


import com.openclassrooms.SafetyNetAlert.dto.StationCoveragePersonInfo;
import com.openclassrooms.SafetyNetAlert.dto.StationCoverageResponse;
import com.openclassrooms.SafetyNetAlert.model.Firestation;

import com.openclassrooms.SafetyNetAlert.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlert.model.Person;
import com.openclassrooms.SafetyNetAlert.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class FirestationService {

    @Autowired
    private final JsonDataLoader jsonDataLoader;

    public List<Firestation> getAllFirestations() {
        return jsonDataLoader.getDataContainer().getFirestations(); // Récupère la liste des casernes
    }

    public Firestation getFirestationByAddress(String address) {
        return jsonDataLoader.getDataContainer().getFirestations().stream()
                .filter(firestation -> firestation.getAddress().equals(address))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Firestation not found for address: " + address));
    }

    public Firestation addFirestation(Firestation firestation) {
        jsonDataLoader.getDataContainer().getFirestations().add(firestation); // Ajoute la caserne
        jsonDataLoader.saveData();
        return firestation;
    }

    public Firestation updateFirestation(Firestation updatedFirestation) {
        Firestation existingFirestation = jsonDataLoader.getDataContainer().getFirestations().stream()
                .filter(firestation -> firestation.getAddress().equals(updatedFirestation.getAddress()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Firestation not found for address: " + updatedFirestation.getAddress()));

        existingFirestation.setStation(updatedFirestation.getStation());
        jsonDataLoader.saveData();
        return existingFirestation;
    }

    public void deleteFirestation(String address) {
        List<Firestation> firestations = jsonDataLoader.getDataContainer().getFirestations();
        firestations.removeIf(firestation -> firestation.getAddress().equals(address)); // Supprime la caserne
        jsonDataLoader.saveData();
    }

    public StationCoverageResponse getPersonsCoveredByStation(int stationNumber) {

        // Vérifie si le numéro de station est valide
        if (stationNumber <= 0) {
            throw new IllegalArgumentException("Le numéro de station doit être un entier positif.");
        }
        // Récupère les adresses desservies par cette station
        List<String> addresses = jsonDataLoader.getDataContainer().getFirestations().stream()
                .filter(firestation -> {
                    try {
                        return Integer.parseInt(firestation.getStation()) == stationNumber;
                    } catch (NumberFormatException e) {
                        throw new DataFormatException("Le numéro de station dans les données JSON est invalide : " + firestation.getStation(), e);
                    }
                })
                .map(Firestation::getAddress)
                .toList();

        if (addresses.isEmpty()) {
            throw new ResourceNotFoundException("Aucune adresse trouvée pour la station numéro : " + stationNumber);
        }

        // Récupère les personnes vivant à ces adresses
        List<Person> persons = jsonDataLoader.getDataContainer().getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .toList();

        if (persons.isEmpty()) {
            throw new ResourceNotFoundException("Aucune personne trouvée pour les adresses desservies par la station numéro : " + stationNumber);
        }

        // Transforme les personnes en objets PersonInfo
        List<StationCoveragePersonInfo> personInfos = persons.stream()
                .map(person -> new StationCoveragePersonInfo(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getPhone()
                ))
                .toList();

        // Compte les adultes et les enfants
        int numberOfAdults = 0;
        int numberOfChildren = 0;
        for (Person person : persons) {
            MedicalRecord record = jsonDataLoader.getDataContainer().getMedicalRecords().stream()
                    .filter(medicalRecord -> medicalRecord.getFirstName().equals(person.getFirstName())
                            && medicalRecord.getLastName().equals(person.getLastName()))
                    .findFirst()
                    .orElse(null);

            if (record == null) {
                throw new ResourceNotFoundException("Dossier médical manquant pour : " + person.getFirstName() + " " + person.getLastName());
            }

            if (CalculateAgeUtil.isUnder18(record.getBirthdate())) {
                numberOfChildren++;
            } else {
                numberOfAdults++;
            }

        }

        // Crée et retourne la réponse
        StationCoverageResponse response = new StationCoverageResponse();
        response.setPersons(personInfos);
        response.setNumberOfAdults(numberOfAdults);
        response.setNumberOfChildren(numberOfChildren);
        return response;
    }



}

