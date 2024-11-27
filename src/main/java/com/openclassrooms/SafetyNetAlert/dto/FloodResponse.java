package com.openclassrooms.SafetyNetAlert.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class FloodResponse {
    private Map<String, List<FloodPersonInfo>> households;

}

