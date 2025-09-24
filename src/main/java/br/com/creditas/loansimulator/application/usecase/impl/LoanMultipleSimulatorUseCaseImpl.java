package br.com.creditas.loansimulator.application.usecase.impl;

import br.com.creditas.loansimulator.application.usecase.LoanMultipleSimulatorUseCase;
import br.com.creditas.loansimulator.application.usecase.LoanSimulatorUseCase;
import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LoanMultipleSimulatorUseCaseImpl implements LoanMultipleSimulatorUseCase {

  private final LoanSimulatorUseCase loanSimulatorUseCase;
  private final Executor batchModeTaskExecutor;

  public LoanMultipleSimulatorUseCaseImpl(
      LoanSimulatorUseCase loanSimulatorUseCase,
      @Qualifier("batchModeTaskExecutor") Executor batchModeTaskExecutor) {
    this.loanSimulatorUseCase = loanSimulatorUseCase;
    this.batchModeTaskExecutor = batchModeTaskExecutor;
  }

  @Override
  @Async("batchModeTaskExecutor")
  public CompletableFuture<List<LoanSimulation>> execute(List<LoanSimulation> simulationsList) {
    if (simulationsList.isEmpty()) {
      return CompletableFuture.completedFuture(List.of());
    }

    List<CompletableFuture<LoanSimulation>> futures =
        simulationsList.stream()
            .map(
                simulation ->
                    CompletableFuture.supplyAsync(
                        () -> loanSimulatorUseCase.execute(simulation), batchModeTaskExecutor))
            .toList();

    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
        .thenApply(v -> futures.stream().map(CompletableFuture::join).toList());
  }
}
