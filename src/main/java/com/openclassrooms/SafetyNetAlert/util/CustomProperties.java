package com.openclassrooms.SafetyNetAlert.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "com.openclassrooms.safety-net-alert")//pour scanner dans le fichier properties toutes les properties avec le prefix
public class CustomProperties {

    private String dataFilePath;
}
