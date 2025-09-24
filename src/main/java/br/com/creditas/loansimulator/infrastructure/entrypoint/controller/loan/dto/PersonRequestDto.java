package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto;

import br.com.creditas.loansimulator.domain.model.Person;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import org.hibernate.validator.constraints.br.CPF;

public record PersonRequestDto(
    @CPF @NotBlank String document, @Past @NotNull LocalDate birthDay, @Email String email) {
  public Person toModel() {
    return new Person(document, birthDay, email);
  }
}
