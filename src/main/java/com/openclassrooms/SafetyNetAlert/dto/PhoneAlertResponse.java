package com.openclassrooms.SafetyNetAlert.dto;

import lombok.*;

import java.util.*;
@Data
@AllArgsConstructor
public class PhoneAlertResponse {
    private List<String> phoneNumbers;

}
