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

/**
 * Service pour gérer les opérations relatives aux casernes de pompiers.
 * Fournit des fonctionnalités pour gérer les casernes, récupérer les informations des personnes couvertes,
 * et effectuer des opérations CRUD sur les casernes.
 */
@Service
@RequiredArgsConstructor
public class FirestationService {

    @Autowired
    private final JsonDataLoader jsonDataLoader;

    /**
     * Récupère la liste de toutes les casernes de pompiers.
     *
     * @return la liste des casernes.
     */
    public List<Firestation> getAllFirestations() {
        return jsonDataLoader.getDataContainer().getFirestations(); // Récupère la liste des casernes
    }

    /**
     * Récupère une caserne de pompiers par son adresse.
     *
     * @param address l'adresse de la caserne à récupérer.
     * @return la caserne correspondante.
     * @throws RuntimeException si aucune caserne n'est trouvée pour l'adresse donnée.
     */
    public Firestation getFirestationByAddress(String address) {
        return jsonDataLoader.getDataContainer().getFirestations().stream()
                .filter(firestation -> firestation.getAddress().equals(address))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Firestation not found for address: " + address));
    }

    /**
     * Ajoute une nouvelle caserne de pompiers.
     *
     * @param firestation la caserne à ajouter.
     * @return la caserne ajoutée.
     */
    public Firestation addFirestation(Firestation firestation) {
        jsonDataLoader.getDataContainer().getFirestations().add(firestation); // Ajoute la caserne
        jsonDataLoader.saveData();
        return firestation;
    }

    /**
     * Met à jour une caserne de pompiers existante.
     *
     * @param updatedFirestation la caserne mise à jour.
     * @return la caserne mise à jour.
     * @throws RuntimeException si aucune caserne n'est trouvée pour l'adresse donnée.
     */
    public Firestation updateFirestation(Firestation updatedFirestation) {
        Firestation existingFirestation = jsonDataLoader.getDataContainer().getFirestations().stream()
                .filter(firestation -> firestation.getAddress().equals(updatedFirestation.getAddress()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Firestation not found for address: " + updatedFirestation.getAddress()));

        existingFirestation.setStation(updatedFirestation.getStation());
        jsonDataLoader.saveData();
        return existingFirestation;
    }

    /**
     * Supprime une caserne de pompiers par son adresse.
     *
     * @param address l'adresse de la caserne à supprimer.
     */
    public boolean deleteFirestation(String address) {
        List<Firestation> firestations = jsonDataLoader.getDataContainer().getFirestations();
        // Vérifie si une caserne correspond à l'adresse
        boolean isDeleted = firestations.removeIf(firestation -> firestation.getAddress().equals(address));

        if (isDeleted) {
            // Sauvegarde les données uniquement si une suppression a été effectuée
            jsonDataLoader.saveData();
        }

        return isDeleted; // Indique si une suppression a eu lieu
    }

    /**
     * Récupère les informations des personnes couvertes par une caserne de pompiers spécifique,
     * ainsi que le nombre d'adultes et d'enfants.
     *
     * @param stationNumber le numéro de la caserne.
     * @return une réponse contenant les informations des personnes couvertes et les statistiques.
     * @throws IllegalArgumentException si le numéro de station est invalide (non positif).
     * @throws ResourceNotFoundException si aucune adresse ou personne n'est trouvée pour la station donnée,
     *                                   ou si des dossiers médicaux sont manquants.
     * @throws DataFormatException si le numéro de station dans les données JSON est au format incorrect.
     */
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

