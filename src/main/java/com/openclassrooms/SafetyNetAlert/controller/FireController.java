package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.dto.*;
import com.openclassrooms.SafetyNetAlert.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fire")
public class FireController {

    @Autowired
    private FireService fireService;

    @GetMapping
    public ResponseEntity<FireResponse> getResidentsByAddress(@RequestParam String address) {
        FireResponse response = fireService.getResidentsByAddress(address);
        return ResponseEntity.ok(response);
    }
}
