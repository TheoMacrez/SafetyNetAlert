package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.dto.*;

import com.openclassrooms.SafetyNetAlert.service.*;

import com.openclassrooms.SafetyNetAlert.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personInfolastName")
public class PersonInfoLastNameController {

    @Autowired
    private PersonInfoLastNameService personInfoService;

    @GetMapping
    public ResponseEntity<List<PersonInfoLastNameResponse>> getPersonsByLastName(@RequestParam String lastName) {
        try {
            List<PersonInfoLastNameResponse> response = personInfoService.getPersonsByLastName(lastName);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

