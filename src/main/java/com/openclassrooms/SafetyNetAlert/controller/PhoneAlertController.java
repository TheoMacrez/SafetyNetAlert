package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.dto.*;
import com.openclassrooms.SafetyNetAlert.service.*;
import com.openclassrooms.SafetyNetAlert.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/phoneAlert")
public class PhoneAlertController {

    @Autowired
    private PhoneAlertService phoneAlertService;

    /**
     * Récupère les numéros de téléphone des habitants couverts par une caserne de pompiers spécifique.
     *
     * @param firestation Le numéro de la caserne de pompiers.
     * @return Une liste des numéros de téléphone associés aux habitants desservis par cette caserne (HTTP 200),
     *         ou un message d'erreur (HTTP 404) si aucune donnée n'est trouvée.
     */
    @GetMapping
    public ResponseEntity<Object> getPhoneNumbersByFirestation(@RequestParam int firestation) {
        try {
            PhoneAlertResponse response = phoneAlertService.getPhoneNumbersByFirestation(firestation);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

