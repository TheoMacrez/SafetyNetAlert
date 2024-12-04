package com.openclassrooms.SafetyNetAlert.service;

import com.openclassrooms.SafetyNetAlert.model.Person;
import com.openclassrooms.SafetyNetAlert.util.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityEmailService {

    @Autowired
    private final JsonDataLoader jsonDataLoader;

    /**
     * Récupère les adresses email des habitants d'une ville donnée.
     *
     * @param city La ville pour laquelle récupérer les emails.
     * @return Une liste d'adresses email des habitants de la ville.
     * @throws ResourceNotFoundException Si aucune personne n'est trouvée dans la ville.
     */
    public List<String> getEmailsByCity(String city) {
        List<String> emails = jsonDataLoader.getDataContainer().getPersons().stream()
                .filter(person -> city.equalsIgnoreCase(person.getCity()))
                .map(Person::getEmail)
                .distinct() // Évite les doublons d'emails.
                .toList();

        if (emails.isEmpty()) {
            throw new ResourceNotFoundException("Aucun habitant trouvé pour la ville : " + city);
        }

        return emails;
    }
}

