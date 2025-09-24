package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import java.math.BigDecimal;

public record LoanSimulationResponseDto(
    BigDecimal totalAmountToPay,
    BigDecimal installmentAmount,
    BigDecimal totalInterest,
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
