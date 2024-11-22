package com.openclassrooms.SafetyNetAlert.util;

import lombok.Setter;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CalculateAgeUtil {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");


    /**
     * -- SETTER --
     *  Définit un `Clock` personnalisé (utile pour les tests).
     */
    @Setter
    private static Clock clock = Clock.systemDefaultZone(); // Par défaut, utilise l'horloge système

    /**
     * Réinitialise l'horloge à l'horloge système.
     */
    public static void resetClock() {
        clock = Clock.systemDefaultZone();
    }
    /**
     * Vérifie si une personne est âgée de moins de 18 ans à partir de sa date de naissance.
     *
     * @param birthDate La date de naissance de la personne en format LocalDate.
     * @return {@code true} si la personne a moins de 18 ans, {@code false} sinon.
     */
    public static boolean isUnder18(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("La date de naissance ne peut pas être null");
        }

        LocalDate currentDate = LocalDate.now(clock);
        int age = Period.between(birthDate, currentDate).getYears();
        return age < 18;
    }

    /**
     * Vérifie si une personne est âgée de moins de 18 ans à partir de sa date de naissance en format String.
     *
     * @param birthDateString La date de naissance de la personne en format "MM/dd/yyyy".
     * @return {@code true} si la personne a moins de 18 ans, {@code false} sinon.
     */
    public static boolean isUnder18(String birthDateString) {
        LocalDate birthDate = parseDate(birthDateString);
        return isUnder18(birthDate);
    }

    /**
     * Calcule l'âge actuel d'une personne à partir de sa date de naissance (String).
     *
     * @param birthDateString La date de naissance en format "MM/dd/yyyy".
     * @return L'âge en années.
     */
    public static int calculateAge(String birthDateString) {
        LocalDate birthDate = parseDate(birthDateString);
        return calculateAge(birthDate);
    }

    /**
     * Calcule l'âge actuel d'une personne à partir de sa date de naissance (LocalDate).
     *
     * @param birthDate La date de naissance de la personne.
     * @return L'âge en années.
     */
    public static int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("La date de naissance ne peut pas être null");
        }

        LocalDate currentDate = LocalDate.now(clock);
        return Period.between(birthDate, currentDate).getYears();
    }

    /**
     * Convertit une chaîne de caractères représentant une date en LocalDate.
     *
     * @param dateString La date sous format "MM/dd/yyyy".
     * @return La date correspondante en LocalDate.
     * @throws IllegalArgumentException si la chaîne est invalide.
     */
    private static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            throw new IllegalArgumentException("La date ne peut pas être null ou vide");
        }

        try {
            return LocalDate.parse(dateString, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez le format MM/dd/yyyy", e);
        }
    }
}
