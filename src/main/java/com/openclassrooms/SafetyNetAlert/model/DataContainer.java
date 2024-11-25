package com.openclassrooms.SafetyNetAlert.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataContainer {
    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalRecords;
}


