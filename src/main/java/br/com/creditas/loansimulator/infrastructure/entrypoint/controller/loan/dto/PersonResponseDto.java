package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto;

import br.com.creditas.loansimulator.domain.model.Person;

public record PersonResponseDto(String document, String birthDay, String email) {
  public PersonResponseDto(Person person) {
    this(person.getDocument(), person.getBirthDay().toString(), person.getEmail());
  }
}
