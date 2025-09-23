package br.com.creditas.loansimulator.infrastructure.event.publisher;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;

public interface EventPublisher {
    void publishEvent(Object source, LoanSimulation loanSimulation);
}
