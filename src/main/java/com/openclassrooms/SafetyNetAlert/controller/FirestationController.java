package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.dto.StationCoverageResponse;
import com.openclassrooms.SafetyNetAlert.model.Firestation;
import com.openclassrooms.SafetyNetAlert.service.FirestationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller pour gérer les opérations CRUD sur les casernes de pompiers (firestations)
 * et les informations associées.
 */
@RestController
@RequestMapping("/firestation")
@RequiredArgsConstructor
public class FirestationController {

    @Autowired
    private final FirestationService firestationService;

    /**
     * Ajoute une nouvelle caserne de pompiers.
     *
     * @param firestation L'objet Firestation à ajouter.
     * @return La caserne créée avec un statut HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
        Firestation createdFirestation = firestationService.addFirestation(firestation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFirestation);
    }

    /**
     * Met à jour une caserne de pompiers existante.
     *
     * @param firestation L'objet Firestation contenant les nouvelles informations.
     * @return La caserne mise à jour.
     */
    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation) {
        Firestation updatedFirestation = firestationService.updateFirestation(firestation);
        if (updatedFirestation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Ou un message d'erreur plus descriptif
        }
        return ResponseEntity.ok(updatedFirestation);
    }

    /**
     * Supprime une caserne de pompiers par adresse.
     *
     * @param address L'adresse de la caserne à supprimer.
     * @return Une réponse vide avec un statut HTTP 204 (No Content).
     */
    @DeleteMapping("/{address}")
    public ResponseEntity<Void> deleteFirestation(@PathVariable String address) {
        boolean isDeleted = firestationService.deleteFirestation(address);

        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build(); // Retourne 404 si aucune caserne n'est trouvée
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * Récupère les informations sur les personnes couvertes par une caserne de pompiers
     * en fonction de son numéro.
     *
     * @param stationNumber Le numéro de la caserne de pompiers.
     * @return Les informations des personnes couvertes.
     */
    @GetMapping
    public ResponseEntity<StationCoverageResponse> getPersonsCoveredByStation(@RequestParam int stationNumber) {
        StationCoverageResponse response = firestationService.getPersonsCoveredByStation(stationNumber);
        return ResponseEntity.ok(response);
    }
}
