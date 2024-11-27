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

