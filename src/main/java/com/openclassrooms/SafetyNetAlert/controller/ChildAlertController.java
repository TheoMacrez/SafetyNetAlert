package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.dto.*;
import com.openclassrooms.SafetyNetAlert.service.ChildAlertService;
import com.openclassrooms.SafetyNetAlert.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChildAlertController {

    @Autowired
    private final ChildAlertService childAlertService;

    /**
     * Endpoint pour récupérer les informations des enfants et son foyer
     * à une adresse donnée.
     *
     * @param address L'adresse pour laquelle récupérer les informations.
     * @return Une réponse contenant les enfants du foyer,
     *         ou un message d'erreur si l'adresse est introuvable.
     */
    @GetMapping("/childAlert")
    public ResponseEntity<Object> getChildrenByAddress(@RequestParam String address) {
        try {
            ChildAlertResponse response = childAlertService.getChildrenByAddress(address);;
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
