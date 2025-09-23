package br.com.creditas.loansimulator.infrastructure.gateway.database.persistence.impl;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.infrastructure.gateway.database.entity.LoanSimulationEntity;
import br.com.creditas.loansimulator.infrastructure.gateway.database.persistence.LoanSimulationPersistence;
import br.com.creditas.loansimulator.infrastructure.gateway.database.repository.LoanSimulationEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanSimulationPersistenceImpl implements LoanSimulationPersistence {

    private final LoanSimulationEntityRepository repository;

    @Override
    public void save(LoanSimulation loanSimulation) {
        repository.save(new LoanSimulationEntity(loanSimulation));
    }
}
