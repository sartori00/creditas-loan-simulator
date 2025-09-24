package br.com.creditas.loansimulator.infrastructure.event;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewLoanCalculatedObservable extends ApplicationEvent {

  private final transient LoanSimulation loanSimulation;

  public NewLoanCalculatedObservable(Object source, LoanSimulation loanSimulation) {
    super(source);
    this.loanSimulation = loanSimulation;
  }
}
