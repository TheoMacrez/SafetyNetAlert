package com.openclassrooms.SafetyNetAlert.dto;

import lombok.*;

import java.util.*;

/**
 * DTO (Data Transfer Object) représentant les données renvoyé à l'URL /phoneAlert?firestation=<firestation_number>
 *
 * Utilisé pour transmettre des données spécifiques concernant
 * la liste des numeros de telephone des personnes désservis par la caserne de pompiers spécifiés.
 */
@Data
@AllArgsConstructor
public class PhoneAlertResponse {
    private List<String> phoneNumbers;

}
