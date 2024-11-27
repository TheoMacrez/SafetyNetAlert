package com.openclassrooms.SafetyNetAlert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO (Data Transfer Object) représentant les informations d'un enfant.
 * Utilisé pour transmettre des données spécifiques concernant les enfants résidant à une adresse donnée.
 */
@Data
@AllArgsConstructor
public class ChildAlertPersonInfo {
    private String firstName;
    private String lastName;
    private int age;
}
