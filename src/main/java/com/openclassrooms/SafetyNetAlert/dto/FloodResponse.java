package com.openclassrooms.SafetyNetAlert.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * DTO (Data Transfer Object) représentant les données renvoyé à l'URL /flood/stations?stations=<a list of
 * station_numbers>
 * Utilisé pour transmettre des données spécifiques concernant la liste des foyers desservies par une caserne.
 */
@Data
@AllArgsConstructor
public class FloodResponse {
    private Map<String, List<FloodPersonInfo>> households;

}

