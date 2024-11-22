package com.openclassrooms.SafetyNetAlert.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data

public class StationCoveragePersonInfo {

    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    public StationCoveragePersonInfo(String firstName, String lastName, String address, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }

}
