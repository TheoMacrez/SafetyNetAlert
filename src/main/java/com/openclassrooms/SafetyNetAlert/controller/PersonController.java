package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.model.*;
import com.openclassrooms.SafetyNetAlert.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller pour gérer les opérations CRUD sur les personnes (Person).
 */
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

    @Autowired
    private final PersonService personService;

    /**
     * Ajoute une nouvelle personne.
     *
     * @param person L'objet Person à ajouter.
     * @return La personne créée avec un statut HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        Person createdPerson = personService.addPerson(person);
        return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
    }

    /**
     * Met à jour une personne existante en fonction de son prénom et de son nom.
     *
     * @param firstName     Le prénom de la personne.
     * @param lastName      Le nom de la personne.
     * @param updatedPerson L'objet Person contenant les nouvelles informations.
     * @return La personne mise à jour avec un statut HTTP 200 (OK).
     */
    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<Person> updatePerson(@PathVariable String firstName,
                                               @PathVariable String lastName,
                                               @RequestBody Person updatedPerson) {
        Person person = personService.updatePerson(firstName, lastName, updatedPerson);
        if (person == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Ou un message d'erreur plus descriptif
        }

        return ResponseEntity.ok(person);

    }

    /**
     * Supprime une personne en fonction de son prénom et de son nom.
     *
     * @param firstName Le prénom de la personne.
     * @param lastName  Le nom de la personne.
     * @return Une réponse vide avec un statut HTTP 204 (No Content).
     */
    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<Void> deletePerson(@PathVariable String firstName,
                                             @PathVariable String lastName) {
        boolean isDeleted = personService.deletePerson(firstName, lastName);
        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build(); // Retourne 404 si aucune caserne n'est trouvée
        }
        return ResponseEntity.noContent().build();
    }
}


