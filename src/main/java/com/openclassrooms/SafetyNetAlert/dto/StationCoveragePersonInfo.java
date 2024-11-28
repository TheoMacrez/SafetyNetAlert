package com.openclassrooms.SafetyNetAlert.dto;

import lombok.*;

import java.util.List;


/**
 * DTO (Data Transfer Object) représentant les informations spécifiques d'une personne.
 * Utilisé pour transmettre des données spécifiques concernant les personnes concernés par une caserne donnée.
 */
@Data
@AllArgsConstructor
public class StationCoveragePersonInfo {

    private String firstName;
    private String lastName;
    private String address;
    private String phone;


}
