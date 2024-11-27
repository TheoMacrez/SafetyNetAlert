package com.openclassrooms.SafetyNetAlert.dto;

import lombok.*;

import java.util.*;

/**
 * DTO (Data Transfer Object) représentant les données renvoyé à l'URL /fire?address=<address>.
 * Utilisé pour transmettre des données spécifiques concernant la caserne de pompiers et les personnes concernés à une adresse donnée.
 */
@Data
@AllArgsConstructor
public class FireResponse {
    private String stationNumber;
    private List<FirePersonInfo> residents;
}
