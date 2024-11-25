package com.openclassrooms.SafetyNetAlert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChildAlertPersonInfo {
    private String firstName;
    private String lastName;
    private int age;
}
