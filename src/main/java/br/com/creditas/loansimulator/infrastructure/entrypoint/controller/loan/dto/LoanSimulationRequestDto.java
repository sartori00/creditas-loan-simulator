package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record LoanSimulationRequestDto(
    @Schema(example = "EUR") @NotNull Currency currency,
    @Schema(example = "1587.5") @Positive @NotNull BigDecimal loanAmount,
    @Schema(example = "48") @Positive @NotNull int qtInstallments,
    @Valid PersonRequestDto person) {
  public LoanSimulation toModel() {
    return new LoanSimulation(currency, loanAmount, qtInstallments, person.toModel());
  }
}
