package com.openclassrooms.SafetyNetAlert.util;

import com.fasterxml.jackson.databind.*;
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


/**
 * Service pour charger et sauvegarder des données JSON dans un fichier.
 * Utilise un {@link ObjectMapper} pour la désérialisation et la sérialisation des objets à partir et vers le format JSON.
 */
@Service
public class JsonDataLoader {


    private final ObjectMapper objectMapper; // Utilisé pour désérialiser le JSON

    private final String filePath;

    @Getter
    @Setter
    private DataContainer dataContainer; // Contient toutes les données

    /**
     * Constructeur qui initialise l'instance de {@link JsonDataLoader} avec un {@link ObjectMapper} et le chemin du fichier de données.
     * Charge également les données au moment de la création de l'instance.
     *
     * @param objectMapper l'objet utilisé pour la désérialisation des données JSON.
     * @param filePath le chemin du fichier JSON où les données sont stockées.
     */
    public JsonDataLoader(ObjectMapper objectMapper,
                          @Value("${com.openclassrooms.safety-net-alert.dataFilePath}") String filePath
                           ) {

        this.objectMapper = objectMapper;
        this.filePath = filePath;
        this.dataContainer = loadData();

    }

    /**
     * Charge les données depuis le fichier JSON et les désérialise dans un objet {@link DataContainer}.
     *
     * @return un objet {@link DataContainer} contenant les données chargées.
     * @throws RuntimeException si le fichier ne peut pas être chargé ou si une erreur de désérialisation se produit.
     */
    private DataContainer loadData() {
        try {

            return objectMapper.readValue(new File(filePath), DataContainer.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON data from file: " + filePath + ". Ensure the file is properly formatted. Error: " + e.getMessage(), e);
        }
    }

    /**
     * Sauvegarde les données actuelles dans le fichier JSON.
     *
     * @throws RuntimeException si la sauvegarde des données échoue.
     */
    public void saveData()
    {
        try {
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(new File(filePath), dataContainer);
            loadData();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save JSON data", e);
        }
    }

}


