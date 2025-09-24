package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto;

import br.com.creditas.loansimulator.domain.model.Person;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PersonResponseDto")
public record PersonResponseDto(
    @Schema(example = "13378450789") String document,
    @Schema(example = "2001-05-05") String birthDay,
    @Schema(example = "fakeemail1@fake.com") String email) {
  public PersonResponseDto(Person person) {
    this(person.getDocument(), person.getBirthDay().toString(), person.getEmail());
  }
}
