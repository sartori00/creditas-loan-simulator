package br.com.creditas.loansimulator.application.usecase.impl;

import br.com.creditas.loansimulator.application.usecase.LoanMultipleSimulatorUseCase;
import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanMultipleSimulatorUseCaseImpl implements LoanMultipleSimulatorUseCase {
    @Override
    public List<LoanSimulation> execute(List<LoanSimulation> list) {
        return List.of();
    }
}
