package com.openclassrooms.SafetyNetAlert.service;

import com.openclassrooms.SafetyNetAlert.dto.*;
import com.openclassrooms.SafetyNetAlert.model.*;
import com.openclassrooms.SafetyNetAlert.util.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Service pour la gestion des alertes téléphoniques des résidents.
 * Permet de récupérer les numéros de téléphone des résidents desservis par une caserne de pompiers.
 */
@Service
@RequiredArgsConstructor
public class PhoneAlertService {

    @Autowired
    private final JsonDataLoader jsonDataLoader;

    /**
     * Récupère les numéros de téléphone des résidents desservis par une caserne.
     *
     * @param firestationNumber le numéro de la caserne pour laquelle on souhaite récupérer les numéros de téléphone des résidents.
     * @return un objet {@link PhoneAlertResponse} contenant la liste des numéros de téléphone des résidents.
     * @throws ResourceNotFoundException si aucune caserne ne correspond au numéro donné ou si aucun résident n'est trouvé pour cette caserne.
     */

    public PhoneAlertResponse getPhoneNumbersByFirestation(int firestationNumber) {

        // Récupérer les adresses desservies par la caserne
        List<String> addresses = jsonDataLoader.getDataContainer().getFirestations().stream()
                .filter(firestation -> Integer.parseInt(firestation.getStation()) == firestationNumber)
                .map(Firestation::getAddress)
                .toList();

        if (addresses.isEmpty()) {
            throw new ResourceNotFoundException("Aucune caserne trouvée pour le numéro : " + firestationNumber);
        }

        // Récupérer les numéros de téléphone des résidents à ces adresses
        List<String> phoneNumbers = jsonDataLoader.getDataContainer().getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .map(Person::getPhone)
                .distinct() // Évite les doublons
                .toList();

        if (phoneNumbers.isEmpty()) {
            throw new ResourceNotFoundException("Aucun résident trouvé pour la caserne : " + firestationNumber);
        }

        // Préparer la réponse
        return new PhoneAlertResponse(phoneNumbers);
    }
}

