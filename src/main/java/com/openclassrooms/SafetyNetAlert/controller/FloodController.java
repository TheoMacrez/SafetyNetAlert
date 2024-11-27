package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.dto.*;
import com.openclassrooms.SafetyNetAlert.service.*;
import com.openclassrooms.SafetyNetAlert.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/flood")
public class FloodController {

    @Autowired
    private FloodService floodService;

    /**
     * Récupère les foyers desservis par les casernes de pompiers spécifiées.
     *
     * @param stations Une liste des numéros des stations de pompiers.
     * @return Une réponse contenant les informations des foyers desservis (HTTP 200)
     *         ou un message d'erreur (HTTP 404) si aucune donnée n'est trouvée.
     */
    @GetMapping("/stations")
    public ResponseEntity<Object> getHouseholdsByStations(@RequestParam List<Integer> stations) {
        try {
            FloodResponse response = floodService.getHouseholdsByStations(stations);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}


