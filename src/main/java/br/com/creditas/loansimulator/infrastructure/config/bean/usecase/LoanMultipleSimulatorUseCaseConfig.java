package br.com.creditas.loansimulator.infrastructure.config.bean.usecase;

import br.com.creditas.loansimulator.application.usecase.impl.LoanMultipleSimulatorUseCaseImpl;
import br.com.creditas.loansimulator.application.usecase.impl.LoanSimulatorUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.VirtualThreadTaskExecutor;

@Configuration
public class LoanMultipleSimulatorUseCaseConfig {

  @Bean
  public LoanMultipleSimulatorUseCaseImpl loanMultipleSimulatorUseCase(
      LoanSimulatorUseCaseImpl loanSimulatorUseCase) {
    return new LoanMultipleSimulatorUseCaseImpl(
        loanSimulatorUseCase, new VirtualThreadTaskExecutor("batchEvent-virtual-thread-"));
  }
}
