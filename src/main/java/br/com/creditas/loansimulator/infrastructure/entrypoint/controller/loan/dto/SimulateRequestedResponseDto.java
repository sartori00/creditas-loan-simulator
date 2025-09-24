package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(name = "SimulateRequestedResponseDto")
public record SimulateRequestedResponseDto(
    @Schema(example = "EUR") Currency currency,
    @Schema(example = "5600") BigDecimal loanAmount,
    @Schema(example = "60") int qtInstallments) {
  public SimulateRequestedResponseDto(LoanSimulation loanSimulation) {
    this(
        loanSimulation.getCurrency(),
        loanSimulation.getLoanAmount(),
        loanSimulation.getQtInstallments());
  }
}
