package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.dto.*;
import com.openclassrooms.SafetyNetAlert.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/phoneAlert")
public class PhoneAlertController {

    @Autowired
    private PhoneAlertService phoneAlertService;

    @GetMapping
    public ResponseEntity<PhoneAlertResponse> getPhoneNumbersByFirestation(@RequestParam int firestation) {
        try {
            PhoneAlertResponse response = phoneAlertService.getPhoneNumbersByFirestation(firestation);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

