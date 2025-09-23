package br.com.creditas.loansimulator.infrastructure.gateway.database.repository;

import br.com.creditas.loansimulator.infrastructure.gateway.database.entity.LoanSimulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanSimulationEntityRepository extends JpaRepository<LoanSimulationEntity, UUID> {
}