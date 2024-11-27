package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.dto.*;
import com.openclassrooms.SafetyNetAlert.service.*;
import com.openclassrooms.SafetyNetAlert.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fire")
public class FireController {

    @Autowired
    private FireService fireService;

    /**
     * Endpoint pour récupérer la liste des habitants vivant à l’adresse donnée ainsi que le
     * numéro de la caserne de pompiers la desservant.
     *
     * @param address L'adresse pour laquelle récupérer les informations des résidents.
     * @return Une réponse contenant les informations des résidents,
     *         ou un message d'erreur si l'adresse n'est pas trouvée.
     */
    @GetMapping
    public ResponseEntity<Object> getResidentsByAddress(@RequestParam String address) {

        try {
            // Appelle le service pour récupérer les informations des résidents
            FireResponse response = fireService.getResidentsByAddress(address);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            // Retourne une réponse avec un statut 404 si l'adresse n'est pas trouvée
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
