package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.dto.ChildAlertResponse;
import com.openclassrooms.SafetyNetAlert.service.ChildAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChildAlertController {

    @Autowired
    private final ChildAlertService childAlertService;

    @GetMapping("/childAlert")
    public ChildAlertResponse getChildrenByAddress(@RequestParam String address) {
        return childAlertService.getChildrenByAddress(address);
    }
}
