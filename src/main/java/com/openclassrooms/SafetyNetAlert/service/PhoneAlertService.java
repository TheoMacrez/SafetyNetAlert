package com.openclassrooms.SafetyNetAlert.service;

import com.openclassrooms.SafetyNetAlert.dto.*;
import com.openclassrooms.SafetyNetAlert.model.*;
import com.openclassrooms.SafetyNetAlert.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class PhoneAlertService {

    @Autowired
    private JsonDataLoader jsonDataLoader;

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

