package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto;

import br.com.creditas.loansimulator.domain.model.Person;

import java.time.LocalDate;

public record PersonResponseDto(
        String document,
        LocalDate birthDay,
        String email
) {
    public PersonResponseDto(Person person) {
        this(person.getDocument(),
                person.getBirthDay(),
                person.getEmail());
    }
}
