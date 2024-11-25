package com.openclassrooms.SafetyNetAlert.dto;


import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class FirePersonInfo {
    private String lastName;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;
}

