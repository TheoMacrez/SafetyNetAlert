package com.openclassrooms.SafetyNetAlert.service;


import com.openclassrooms.SafetyNetAlert.model.Person;

import com.openclassrooms.SafetyNetAlert.util.JsonDataLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * Service pour gérer les opérations relatives aux personnes.
 * Permet de récupérer, ajouter, mettre à jour et supprimer des personnes
 * et effectuer des opérations CRUD dessus.
 */
@Service
@RequiredArgsConstructor
public class PersonService {

    @Autowired
    private final JsonDataLoader jsonDataLoader;

    /**
     * Récupère toutes les personnes.
     *
     * @return une liste de toutes les {@link Person} présentes dans la source de données.
     */
    public List<Person> getAllPersons() {
        return jsonDataLoader.getDataContainer().getPersons(); // Récupère la liste des personnes
    }

    /**
     * Récupère une personne par son prénom et son nom de famille.
     *
     * @param firstName le prénom de la personne à rechercher.
     * @param lastName le nom de famille de la personne à rechercher.
     * @return la {@link Person} correspondant au prénom et nom donnés.
     * @throws RuntimeException si la personne n'est pas trouvée dans la source de données.
     */
    public Person getPerson(String firstName, String lastName)
    {
        List<Person> persons = jsonDataLoader.getDataContainer().getPersons();
        return persons.stream()
                .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Person not found"));
    }

    /**
     * Ajoute une nouvelle personne dans la source de données.
     *
     * @param person la {@link Person} à ajouter.
     * @return la personne ajoutée.
     */
    public Person addPerson(Person person) {
        jsonDataLoader.getDataContainer().getPersons().add(person); // Ajoute la personne dans la liste en mémoire
        jsonDataLoader.saveData(); // Sauvegarde dans le fichier JSON
        return person;
    }

    /**
     * Met à jour les informations d'une personne existante.
     *
     * @param firstName le prénom de la personne à mettre à jour.
     * @param lastName le nom de famille de la personne à mettre à jour.
     * @param updatedPerson l'objet {@link Person} contenant les nouvelles informations.
     * @return la personne mise à jour.
     * @throws RuntimeException si la personne n'est pas trouvée dans la source de données.
     */
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

    /**
     * Supprime une personne de la source de données.
     *
     * @param firstName le prénom de la personne à supprimer.
     * @param lastName le nom de famille de la personne à supprimer.
     * @throws RuntimeException si la personne n'est pas trouvée dans la source de données.
     */
    public void deletePerson(String firstName, String lastName) {
        List<Person> persons = jsonDataLoader.getDataContainer().getPersons();
        persons.removeIf(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName));
        jsonDataLoader.saveData(); // Sauvegarde dans le fichier JSON
    }


}
