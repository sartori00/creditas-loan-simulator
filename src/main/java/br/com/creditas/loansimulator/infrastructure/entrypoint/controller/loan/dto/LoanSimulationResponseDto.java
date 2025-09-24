package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(name = "LoanSimulationResponseDto")
public record LoanSimulationResponseDto(
    @Schema(example = "10507.68") BigDecimal totalAmountToPay,
    @Schema(example = "218.91") BigDecimal installmentAmount,
    @Schema(example = "617.56") BigDecimal totalInterest,
    PersonResponseDto person,
    SimulateRequestedResponseDto simulationRequested) {

  public LoanSimulationResponseDto(LoanSimulation loanSimulation) {
    this(
        loanSimulation.getTotalAmountToPay(),
        loanSimulation.getInstallmentAmount(),
        loanSimulation.getTotalInterest(),
        new PersonResponseDto(loanSimulation.getPerson()),
        new SimulateRequestedResponseDto(loanSimulation));
  }
}
