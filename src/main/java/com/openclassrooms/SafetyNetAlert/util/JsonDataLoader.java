package com.openclassrooms.SafetyNetAlert.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlert.model.DataContainer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;


@Service
public class JsonDataLoader {


    private final ObjectMapper objectMapper; // Utilisé pour désérialiser le JSON

    private final String filePath;

    @Getter
    @Setter
    private DataContainer dataContainer; // Contient toutes vos données




    public JsonDataLoader(ObjectMapper objectMapper,
                          @Value("${com.openclassrooms.safety-net-alert.dataFilePath}") String filePath
                           ) {

        this.objectMapper = objectMapper;
        this.filePath = filePath;
        this.dataContainer = loadData();

    }

    private DataContainer loadData() {
        try {

            return objectMapper.readValue(filePath, DataContainer.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON data", e);
        }
    }

    public void saveData()
    {
        try {

            objectMapper.writeValue(new File(filePath), dataContainer);
            loadData();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save JSON data", e);
        }
    }

}


