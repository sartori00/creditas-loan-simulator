package br.com.creditas.loansimulator.infrastructure.gateway.database.repository;

import br.com.creditas.loansimulator.infrastructure.gateway.database.entity.LoanSimulationEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanSimulationEntityRepository extends JpaRepository<LoanSimulationEntity, UUID> {}
