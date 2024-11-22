package com.openclassrooms.SafetyNetAlert.dto;

import lombok.Data;

import java.util.List;

@Data
public class StationCoverageResponse {

    private List<StationCoveragePersonInfo> persons;
    private int numberOfAdults;
    private int numberOfChildren;
}
