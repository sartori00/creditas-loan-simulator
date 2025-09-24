package br.com.creditas.loansimulator.infrastructure.event.subscriber;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.Person;
import br.com.creditas.loansimulator.infrastructure.event.NewLoanCalculatedObservable;
import br.com.creditas.loansimulator.infrastructure.gateway.database.persistence.LoanSimulationPersistence;
import br.com.creditas.loansimulator.infrastructure.gateway.database.persistence.PersonPersistence;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveOnDbSubscriber implements ApplicationListener<NewLoanCalculatedObservable> {

  private final PersonPersistence personPersistence;
  private final LoanSimulationPersistence loanSimulationPersistence;

  @Override
  @Async("eventSubscribersTaskExecutor")
  @Transactional
  public void onApplicationEvent(NewLoanCalculatedObservable event) {
    var loanSimulation = event.getLoanSimulation();
    log.info(
        "Saving async Loan Simulation for person document {}",
        loanSimulation.getPerson().getDocument());

    var personOptional = this.findPersonByDocument(loanSimulation.getPerson().getDocument());

    personOptional.ifPresentOrElse(
        loanSimulation::setPerson,
        () -> {
          var person = this.savePerson(loanSimulation.getPerson());
          loanSimulation.setPerson(person);
        });

    this.saveLoanSimulation(loanSimulation);
  }

  @Override
  public boolean supportsAsyncExecution() {
    return ApplicationListener.super.supportsAsyncExecution();
  }

  private void saveLoanSimulation(LoanSimulation loanSimulation) {
    loanSimulationPersistence.save(loanSimulation);
  }

  private Optional<Person> findPersonByDocument(String document) {
    return personPersistence.findByDocument(document);
  }

  private Person savePerson(Person person) {
    return personPersistence.save(person);
  }
}
