package com.openclassrooms.SafetyNetAlert.dto;

import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class FireResponse {
    private String stationNumber;
    private List<FirePersonInfo> residents;
}
