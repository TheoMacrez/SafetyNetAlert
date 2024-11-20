package com.openclassrooms.SafetyNetAlert.controller;

import com.openclassrooms.SafetyNetAlert.model.Person;
import com.openclassrooms.SafetyNetAlert.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

    @Autowired
    private final PersonService personService;


    // Ajouter une nouvelle personne
    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        Person createdPerson = personService.addPerson(person);
        return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
    }

    // Mettre à jour une personne existante
    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<Person> updatePerson(@PathVariable String firstName, @PathVariable String lastName, @RequestBody Person updatedPerson) {
        Person person = personService.updatePerson(firstName, lastName, updatedPerson);
        return ResponseEntity.ok(person);
    }

    // Supprimer une personne
    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<Void> deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
        personService.deletePerson(firstName, lastName);
        return ResponseEntity.noContent().build();
    }
}

