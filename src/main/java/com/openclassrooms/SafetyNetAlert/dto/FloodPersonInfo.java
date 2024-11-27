package com.openclassrooms.SafetyNetAlert.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class FloodPersonInfo {
    private String name;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;

}

