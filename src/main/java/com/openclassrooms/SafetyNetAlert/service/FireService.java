package com.openclassrooms.SafetyNetAlert.service;

import com.openclassrooms.SafetyNetAlert.dto.*;
import com.openclassrooms.SafetyNetAlert.model.*;
import com.openclassrooms.SafetyNetAlert.util.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Service pour gérer les informations relatives aux incendies.
 * Permet de récupérer les résidents vivant à une adresse spécifique et leur lien avec la caserne de pompiers.
 */
@Service
@RequiredArgsConstructor
public class FireService {

    @Autowired
    private final JsonDataLoader jsonDataLoader;

    /**
     * Récupère les informations des résidents vivant à une adresse spécifique,
     * ainsi que le numéro de la caserne de pompiers desservant cette adresse.
     *
     * @param address l'adresse à analyser.
     * @return une réponse contenant le numéro de la caserne et les informations des résidents.
     * @throws ResourceNotFoundException si l'adresse n'est associée à aucune caserne ou si aucun résident n'est trouvé.
     */
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

        // Transforme les personnes en objets FirePersonInfo
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

