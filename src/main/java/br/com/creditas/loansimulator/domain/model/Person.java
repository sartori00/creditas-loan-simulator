package br.com.creditas.loansimulator.domain.model;

import java.time.LocalDate;
import java.util.UUID;

public class Person {
    private UUID id;
    private final String document;
    private final LocalDate birthDay;
    private final String email;

    public Person(String document, LocalDate birthDay, String email) {
        this.document = document;
        this.birthDay = birthDay;
        this.email = email;
    }

    public Person(UUID id, String document, LocalDate birthDay, String email) {
        this.id = id;
        this.document = document;
        this.birthDay = birthDay;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getDocument() {
        return document;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public String getEmail() {
        return email;
    }
}
