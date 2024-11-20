package com.openclassrooms.SafetyNetAlert;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlert.model.Person;
import com.openclassrooms.SafetyNetAlert.service.PersonService;
import com.openclassrooms.SafetyNetAlert.util.JsonDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
class PersonServiceTest {

    @Autowired
    private JsonDataLoader jsonDataLoader;

    private PersonService personService;

    @BeforeEach
    void setUp() throws IOException {
        // Préparer une copie fraîche de testData.json avant chaque test
        File source = new File("src/test/resources/testDataOriginal.json");
        File destination = new File("src/test/resources/testData.json");
        FileSystemUtils.copyRecursively(source, destination);

        jsonDataLoader.loadDataFromFile("src/test/resources/testData.json");
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
        Person updatedPerson = new Person("Jane", "Smith", "Updated Address", "Updated City", "99999", "555-9999", "jane.updated@example.com");
        Person person = personService.updatePerson("Jane", "Smith", updatedPerson);

        assertThat(person.getAddress()).isEqualTo("Updated Address");
        assertThat(person.getCity()).isEqualTo("Updated City");
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

