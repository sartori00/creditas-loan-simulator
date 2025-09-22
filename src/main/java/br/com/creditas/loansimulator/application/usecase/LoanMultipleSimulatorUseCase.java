package br.com.creditas.loansimulator.application.usecase;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;

import java.util.List;

public interface LoanMultipleSimulatorUseCase {
    List<LoanSimulation> execute(List<LoanSimulation> list);
}
