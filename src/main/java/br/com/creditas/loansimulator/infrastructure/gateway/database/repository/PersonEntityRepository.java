package br.com.creditas.loansimulator.infrastructure.gateway.database.repository;

import br.com.creditas.loansimulator.infrastructure.gateway.database.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PersonEntityRepository extends JpaRepository<PersonEntity, UUID> {
    Optional<PersonEntity> findByDocument(String document);
}