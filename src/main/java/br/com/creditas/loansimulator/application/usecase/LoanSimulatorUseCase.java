package br.com.creditas.loansimulator.application.usecase;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;

public interface LoanSimulatorUseCase {
    LoanSimulation execute(LoanSimulation loanSimulation);
}
