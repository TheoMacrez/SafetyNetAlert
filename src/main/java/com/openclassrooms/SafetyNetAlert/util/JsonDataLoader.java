package com.openclassrooms.SafetyNetAlert.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlert.model.DataContainer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;


@Service
public class JsonDataLoader {

    @Autowired
    private final ObjectMapper objectMapper; // Utilisé pour désérialiser le JSON

    @Getter
    @Setter
    private DataContainer dataContainer; // Contient toutes vos données

    @Autowired
    private CustomProperties propFilePath;

    public JsonDataLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.dataContainer = loadData();
    }

    private DataContainer loadData() {
        try {
            assert propFilePath != null;
            return objectMapper.readValue(new File(propFilePath.getDataFilePath()), DataContainer.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON data", e);
        }
    }

    public void loadDataFromFile(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            dataContainer = objectMapper.readValue(new File(filePath), DataContainer.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON data", e);
        }
    }

    public void saveData()
    {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(propFilePath.getDataFilePath()), dataContainer);
            loadData();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save JSON data", e);
        }
    }

}


