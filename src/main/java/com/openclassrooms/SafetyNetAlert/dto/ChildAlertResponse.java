package com.openclassrooms.SafetyNetAlert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChildAlertResponse {
    private List<ChildAlertPersonInfo> children;
    private List<String> otherHouseholdMembers; // Liste des noms des autres membres du foyer
}

