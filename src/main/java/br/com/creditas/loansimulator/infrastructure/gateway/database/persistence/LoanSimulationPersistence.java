package br.com.creditas.loansimulator.infrastructure.gateway.database.persistence;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;

public interface LoanSimulationPersistence {
  void save(LoanSimulation loanSimulation);
}
