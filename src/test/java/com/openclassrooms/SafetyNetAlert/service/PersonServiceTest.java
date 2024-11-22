package com.openclassrooms.SafetyNetAlert.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlert.model.Person;
import com.openclassrooms.SafetyNetAlert.util.JsonDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
class PersonServiceTest {

    private PersonService personService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {


        // Chemin vers le fichier original dans resources
        Path originalFilePath = Path.of("src/test/resources/testDataOriginal.json");

        // Chemin vers le fichier temporaire dans le répertoire temporaire
        Path tempFilePath = tempDir.resolve("testData.json");

        // Copier le fichier original dans le répertoire temporaire
        Files.copy(originalFilePath, tempFilePath);

        // Initialiser JsonDataLoader avec le chemin du fichier temporaire
        JsonDataLoader jsonDataLoader = new JsonDataLoader(new ObjectMapper(), tempFilePath.toString());

        personService = new PersonService(jsonDataLoader);
    }


    @Test
    void getAllPersons_ShouldReturnAllPersons() {
        List<Person> persons = personService.getAllPersons();

        assertThat(persons).isNotEmpty();
        assertThat(persons.size()).isEqualTo(23); // Si le fichier contient 3 personnes initialement
    }

    @Test
    void addPerson_ShouldAddNewPerson() {
        Person newPerson = new Person("John", "Doe", "123 New Street", "Culver", "97451", "541-874-7458", "john.doe@example.com");
        personService.addPerson(newPerson);

        List<Person> persons = personService.getAllPersons();
        assertThat(persons).contains(newPerson);
        assertThat(persons.size()).isEqualTo(24); // Si initialement il y avait 3 personnes
    }

    @Test
    void updatePerson_ShouldUpdateExistingPerson() {
        Person newPerson = new Person("Jane", "Smith", "new Address", "new City", "99999", "555-9999", "jane.updated@example.com");
        personService.addPerson(newPerson);
        newPerson.setAddress("Updated Address");
        newPerson.setCity("Updated City");
        personService.updatePerson("Jane", "Smith", newPerson);

        Person personToTest = personService.getPerson("Jane","Smith");

        assertThat(personToTest.getAddress()).isEqualTo("Updated Address");
        assertThat(personToTest.getCity()).isEqualTo("Updated City");
    }

    @Test
    void deletePerson_ShouldRemovePerson() {

        // Vérifier si "John Doe" est déjà présent
        try {
            Person existingPerson = personService.getPerson("John", "Doe");
            // Si "John Doe" existe, on peut directement passer au test
            assertThat(existingPerson).isNotNull();
        } catch (RuntimeException e) {
            // Si "John Doe" n'existe pas, on l'ajoute avant de tester la suppression
            Person newPerson = new Person("John", "Doe", "123 New Street", "Culver", "97451", "541-874-7458", "john.doe@example.com");
            personService.addPerson(newPerson);
        }

        personService.deletePerson("John", "Doe");

        List<Person> persons = personService.getAllPersons();
        assertThat(persons.stream().anyMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe"))).isFalse();
    }

    @Test
    void updatePerson_ShouldThrowExceptionIfPersonNotFound() {
        Person updatedPerson = new Person("Non", "Existent", "Address", "City", "00000", "000-0000", "nonexistent@example.com");

        Exception exception = assertThrows(RuntimeException.class, () ->
                personService.updatePerson("Non", "Existent", updatedPerson));

        assertThat(exception.getMessage()).isEqualTo("Person not found");
    }
}

