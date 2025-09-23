package br.com.creditas.loansimulator.infrastructure.event.publisher;

import br.com.creditas.loansimulator.application.events.EventPublisher;
import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.infrastructure.event.NewLoanCalculatedObservable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisherImpl implements EventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishEvent(Object source, LoanSimulation loanSimulation) {
        eventPublisher.publishEvent(new NewLoanCalculatedObservable(this, loanSimulation));
    }
}
