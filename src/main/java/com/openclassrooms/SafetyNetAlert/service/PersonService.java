package com.openclassrooms.SafetyNetAlert.service;


import com.openclassrooms.SafetyNetAlert.model.Person;

import com.openclassrooms.SafetyNetAlert.util.JsonDataLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    @Autowired
    private final JsonDataLoader jsonDataLoader;

    public List<Person> getAllPersons() {
        return jsonDataLoader.getDataContainer().getPersons(); // Récupère la liste des personnes
    }

    public Person getPerson(String firstName, String lastName)
    {
        List<Person> persons = jsonDataLoader.getDataContainer().getPersons();
        return persons.stream()
                .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Person not found"));
    }

    public Person addPerson(Person person) {
        jsonDataLoader.getDataContainer().getPersons().add(person); // Ajoute la personne dans la liste en mémoire
        jsonDataLoader.saveData(); // Sauvegarde dans le fichier JSON
        return person;
    }

    public Person updatePerson(String firstName, String lastName, Person updatedPerson) {
        List<Person> persons = jsonDataLoader.getDataContainer().getPersons();
        Person person = persons.stream()
                .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Person not found"));

        person.setAddress(updatedPerson.getAddress());
        person.setCity(updatedPerson.getCity());
        person.setZip(updatedPerson.getZip());
        person.setPhone(updatedPerson.getPhone());
        person.setEmail(updatedPerson.getEmail());
        jsonDataLoader.saveData(); // Sauvegarde dans le fichier JSON
        return person;
    }

    public void deletePerson(String firstName, String lastName) {
        List<Person> persons = jsonDataLoader.getDataContainer().getPersons();
        persons.removeIf(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName));
        jsonDataLoader.saveData(); // Sauvegarde dans le fichier JSON
    }


}
