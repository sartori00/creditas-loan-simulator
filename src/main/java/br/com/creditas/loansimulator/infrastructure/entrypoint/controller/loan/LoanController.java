package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan;

import br.com.creditas.loansimulator.application.usecase.LoanMultipleSimulatorUseCase;
import br.com.creditas.loansimulator.application.usecase.LoanSimulatorUseCase;
import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto.LoanSimulationRequestDto;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto.LoanSimulationResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/loan")
public class LoanController {

    private final LoanSimulatorUseCase loanSimulatorUseCase;
    private final LoanMultipleSimulatorUseCase loanMultipleSimulatorUseCase;

    @PostMapping
    public ResponseEntity<LoanSimulationResponseDto> simulateALoan(@RequestBody @Valid LoanSimulationRequestDto dto){
        var loanSimulation = loanSimulatorUseCase.execute(dto.toModel());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new LoanSimulationResponseDto(loanSimulation));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<LoanSimulationResponseDto>> simulateMultipleLoans(@RequestBody @Valid List<LoanSimulationRequestDto> dto){
        List<LoanSimulation> simulations = loanMultipleSimulatorUseCase.execute(dto.stream().map(LoanSimulationRequestDto::toModel).toList());
        return ResponseEntity.status(HttpStatus.OK)
                .body(simulations.stream().map(LoanSimulationResponseDto::new).toList());
    }
}
