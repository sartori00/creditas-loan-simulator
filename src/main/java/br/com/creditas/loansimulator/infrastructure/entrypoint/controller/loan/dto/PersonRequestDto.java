package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto;

import br.com.creditas.loansimulator.domain.model.Person;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import org.hibernate.validator.constraints.br.CPF;

public record PersonRequestDto(
    @Schema(example = "94829138092") @CPF @NotBlank String document,
    @Schema(example = "1955-05-05") @Past @NotNull LocalDate birthDay,
    @Schema(example = "fakeemail@fake.com") @Email String email) {
  public Person toModel() {
    return new Person(document, birthDay, email);
  }
}
