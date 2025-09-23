package br.com.creditas.loansimulator.infrastructure.event.subscriber;

import br.com.creditas.loansimulator.infrastructure.event.NewLoanCalculatedObservable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendEmailSubscriber implements ApplicationListener<NewLoanCalculatedObservable> {

    @Override
    @Async("eventSubscribersTaskExecutor")
    public void onApplicationEvent(NewLoanCalculatedObservable event) {
        var loanSimulation = event.getLoanSimulation();
        log.info("Enviando email para {}", loanSimulation.getPerson().getEmail());
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
