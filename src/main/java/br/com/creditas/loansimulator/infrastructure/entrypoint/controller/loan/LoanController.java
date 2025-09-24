package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan;

import br.com.creditas.loansimulator.application.usecase.LoanMultipleSimulatorUseCase;
import br.com.creditas.loansimulator.application.usecase.LoanSimulatorUseCase;
import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto.LoanSimulationRequestDto;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto.LoanSimulationResponseDto;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.openapi.LoanControllerOpenApi;
import br.com.creditas.loansimulator.infrastructure.event.publisher.EventPublisher;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/loan")
public class LoanController implements LoanControllerOpenApi {

  private final LoanSimulatorUseCase loanSimulatorUseCase;
  private final LoanMultipleSimulatorUseCase loanMultipleSimulatorUseCase;
  private final EventPublisher eventPublisher;

  @Override
  @PostMapping
  public ResponseEntity<LoanSimulationResponseDto> simulateALoan(
      @RequestBody @Valid LoanSimulationRequestDto dto) {
    var loanSimulation = loanSimulatorUseCase.execute(dto.toModel());

    eventPublisher.publishEvent(this, loanSimulation);
    return ResponseEntity.status(HttpStatus.OK).body(new LoanSimulationResponseDto(loanSimulation));
  }

  @Override
  @PostMapping("/batch")
  public CompletableFuture<ResponseEntity<List<LoanSimulationResponseDto>>> simulateMultipleLoans(
      @RequestBody @Valid List<LoanSimulationRequestDto> dto) {
    final List<LoanSimulation> processedSimulations = new ArrayList<>();
    List<LoanSimulation> simulationsToProcess =
        dto.stream().map(LoanSimulationRequestDto::toModel).toList();

    CompletableFuture<List<LoanSimulation>> simulationsFuture =
        loanMultipleSimulatorUseCase.execute(simulationsToProcess);

    CompletableFuture<List<LoanSimulationResponseDto>> responseDtosFuture =
        simulationsFuture.thenApply(
            simulations ->
                simulations.stream()
                    .map(
                        loanSimulation -> {
                          processedSimulations.add(loanSimulation);
                          return new LoanSimulationResponseDto(loanSimulation);
                        })
                    .toList());

    CompletableFuture<ResponseEntity<List<LoanSimulationResponseDto>>> finalResponseEntityFuture =
        responseDtosFuture.thenApply(
            response -> ResponseEntity.status(HttpStatus.OK).body(response));

    finalResponseEntityFuture.whenComplete(
        (responseEntity, throwable) -> {
          if (throwable == null) {
            CompletableFuture.runAsync(() -> this.notify(processedSimulations));
          }
        });

    return finalResponseEntityFuture;
  }

  private void notify(List<LoanSimulation> loanSimulations) {
    loanSimulations.forEach(loanSimulation -> eventPublisher.publishEvent(this, loanSimulation));
  }
}
