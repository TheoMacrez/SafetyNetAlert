package com.openclassrooms.SafetyNetAlert.repository;

import com.openclassrooms.SafetyNetAlert.model.Firestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FirestationRepository extends JpaRepository<Firestation, Long> {
    Optional<Firestation> findByAddress(String address);
}
