package br.com.creditas.loansimulator.domain.model;

import java.time.LocalDate;

public class Person {
    private final String document;
    private final LocalDate birthDay;
    private final String email;

    public Person(String document, LocalDate birthDay, String email) {
        this.document = document;
        this.birthDay = birthDay;
        this.email = email;
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
