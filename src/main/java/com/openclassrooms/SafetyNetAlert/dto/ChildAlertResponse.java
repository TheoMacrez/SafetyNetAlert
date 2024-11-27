package com.openclassrooms.SafetyNetAlert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO (Data Transfer Object) représentant les données renvoyé à l'URL /childAlert?address=<address> .
 * Utilisé pour transmettre des données spécifiques concernant les enfants et les autres résidents résidant  à une adresse donnée.
 */
@Data
@AllArgsConstructor
public class ChildAlertResponse {
    private List<ChildAlertPersonInfo> children;
    private List<String> otherHouseholdMembers; // Liste des noms des autres membres du foyer
}

