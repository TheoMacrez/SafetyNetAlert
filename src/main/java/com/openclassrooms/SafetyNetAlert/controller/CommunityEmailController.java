package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.service.CommunityEmailService;
import com.openclassrooms.SafetyNetAlert.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/communityEmail")
public class CommunityEmailController {

    @Autowired
    private CommunityEmailService communityEmailService;

    /**
     * Endpoint pour récupérer les adresses email des habitants d'une ville.
     *
     * @param city La ville pour laquelle récupérer les emails.
     * @return Une réponse contenant la liste d'adresses email.
     */
    @GetMapping
    public ResponseEntity<List<String>> getEmailsByCity(@RequestParam String city) {
        try {
            List<String> emails = communityEmailService.getEmailsByCity(city);
            return ResponseEntity.ok(emails);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
