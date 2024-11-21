package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.model.Firestation;
import com.openclassrooms.SafetyNetAlert.service.FirestationService;
import com.openclassrooms.SafetyNetAlert.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/firestation")
@RequiredArgsConstructor
public class FirestationController {

    @Autowired
    private final FirestationService firestationService;


    @PostMapping
    public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
        Firestation createdFirestation = firestationService.addFirestation(firestation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFirestation);
    }

    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation) {
        Firestation updatedFirestation = firestationService.updateFirestation(firestation);
        return ResponseEntity.ok(updatedFirestation);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFirestation(@PathVariable String address) {
        firestationService.deleteFirestation(address);
        return ResponseEntity.noContent().build();
    }
}

