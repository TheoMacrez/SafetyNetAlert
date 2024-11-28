package com.openclassrooms.SafetyNetAlert.dto;

import lombok.*;

import java.util.List;

/**
 * DTO (Data Transfer Object) représentant les informations spécifiques d'une personne.
 * Utilisé pour transmettre des données spécifiques concernant les personnes concernés par une caserne donnée.
 */
@Data
@AllArgsConstructor
public class FloodPersonInfo {
    private String name;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;

}

