package com.openclassrooms.SafetyNetAlert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO (Data Transfer Object) représentant les données renvoyé à l'URL /personInfolastName=<lastName>
 * Utilisé pour transmettre des données spécifiques concernant la liste des personnes avec le nom de famille spécifié.
 */
@Data
@AllArgsConstructor
public class PersonInfoLastNameResponse {
    private String firstName;
    private String lastName;
    private String address;
    private int age;
    private String email;
    private List<String> medications;
    private List<String> allergies;
}

