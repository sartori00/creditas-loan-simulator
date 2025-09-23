package br.com.creditas.loansimulator.application.usecase;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface LoanMultipleSimulatorUseCase {
    CompletableFuture<List<LoanSimulation>> execute(List<LoanSimulation> simulationsList);
}
