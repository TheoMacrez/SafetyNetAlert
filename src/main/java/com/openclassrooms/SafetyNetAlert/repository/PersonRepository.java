package com.openclassrooms.SafetyNetAlert.repository;

import com.openclassrooms.SafetyNetAlert.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByFirstNameAndLastName(String firstName, String lastName);
    List<Person> findByCity(String city);
}

