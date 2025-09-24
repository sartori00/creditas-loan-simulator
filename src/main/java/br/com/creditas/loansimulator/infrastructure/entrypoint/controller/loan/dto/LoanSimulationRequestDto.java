package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record LoanSimulationRequestDto(
    @NotNull Currency currency,
    @Positive @NotNull BigDecimal loanAmount,
    @Positive @NotNull int qtInstallments,
    @Valid PersonRequestDto person) {
  public LoanSimulation toModel() {
    return new LoanSimulation(currency, loanAmount, qtInstallments, person.toModel());
  }
}
