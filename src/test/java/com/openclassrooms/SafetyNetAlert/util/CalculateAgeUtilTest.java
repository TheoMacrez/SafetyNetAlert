package com.openclassrooms.SafetyNetAlert.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class CalculateAgeUtilTest {

    private final static Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2024-11-22T00:00:00Z"), // Date fixe pour les tests
            ZoneId.systemDefault()
    );

    @AfterEach
    void resetClock() {
        CalculateAgeUtil.resetClock(); // Réinitialise l'horloge après chaque test
    }

    @Test
    void isUnder18_shouldReturnTrueForMinor() {
        CalculateAgeUtil.setClock(FIXED_CLOCK);
        LocalDate birthDate = LocalDate.of(2010, 5, 15);
        assertTrue(CalculateAgeUtil.isUnder18(birthDate));
    }

    @Test
    void isUnder18_shouldReturnFalseForAdult() {
        CalculateAgeUtil.setClock(FIXED_CLOCK);
        LocalDate birthDate = LocalDate.of(1990, 5, 15);
        assertFalse(CalculateAgeUtil.isUnder18(birthDate));
    }

    @Test
    void calculateAge_shouldReturnCorrectAge() {
        CalculateAgeUtil.setClock(FIXED_CLOCK);
        LocalDate birthDate = LocalDate.of(2000, 1, 1);

        int age = CalculateAgeUtil.calculateAge(birthDate);
        assertEquals(24, age);
    }

    @Test
    void methods_shouldThrowExceptionForNullBirthDate() {
        assertThrows(IllegalArgumentException.class, () -> CalculateAgeUtil.isUnder18((LocalDate) null));
        assertThrows(IllegalArgumentException.class, () -> CalculateAgeUtil.calculateAge((String) null));
    }

    @Test
    void isUnder18_withString_shouldReturnTrueForMinor() {
        assertTrue(CalculateAgeUtil.isUnder18("12/25/2010"));
    }

    @Test
    void isUnder18_withString_shouldReturnFalseForAdult() {
        assertFalse(CalculateAgeUtil.isUnder18("12/25/1990"));
    }

    @Test
    void calculateAge_withString_shouldReturnCorrectAge() {
        assertEquals(13, CalculateAgeUtil.calculateAge("12/25/2010"));
    }

    @Test
    void parseDate_withInvalidFormat_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> CalculateAgeUtil.isUnder18("25/12/2010"));
    }
}

