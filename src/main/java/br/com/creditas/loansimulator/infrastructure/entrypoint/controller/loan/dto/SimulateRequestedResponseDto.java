package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.enums.Currency;

import java.math.BigDecimal;

public record SimulateRequestedResponseDto(Currency currency,
                                           BigDecimal loanAmount,
                                           int qtInstallments) {
    public SimulateRequestedResponseDto(LoanSimulation loanSimulation) {
        this(loanSimulation.getCurrency(),
                loanSimulation.getLoanAmount(),
                loanSimulation.getQtInstallments());
    }
}
