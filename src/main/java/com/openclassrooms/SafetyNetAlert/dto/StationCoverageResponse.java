package com.openclassrooms.SafetyNetAlert.dto;

import lombok.Data;

import java.util.List;


/**
 * DTO (Data Transfer Object) représentant les données renvoyé à l'URL /firestation?stationNumber=<station_number>
 * Utilisé pour transmettre des données spécifiques concernant la liste des personnes couvertes par la station de pompier spécifié par son numéro.
 */
@Data
public class StationCoverageResponse {

    private List<StationCoveragePersonInfo> persons;
    private int numberOfAdults;
    private int numberOfChildren;
}
