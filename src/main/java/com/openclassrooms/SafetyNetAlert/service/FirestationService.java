package com.openclassrooms.SafetyNetAlert.service;


import com.openclassrooms.SafetyNetAlert.model.Firestation;

import com.openclassrooms.SafetyNetAlert.util.JsonDataLoader;
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


}

