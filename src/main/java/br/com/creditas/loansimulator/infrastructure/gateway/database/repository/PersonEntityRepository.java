package br.com.creditas.loansimulator.infrastructure.gateway.database.repository;

import br.com.creditas.loansimulator.infrastructure.gateway.database.entity.PersonEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public interface PersonEntityRepository extends JpaRepository<PersonEntity, UUID> {
    Optional<PersonEntity> findByDocument(String document);

    @Transactional
    @Query(value = """
            INSERT INTO PERSON (DOCUMENT, BIRTHDAY, EMAIL, CREATED_AT, UPDATED_AT, CREATED_BY, UPDATED_BY)
            VALUES (:document, :birthDay, :email, :updatedAt, :updatedAt, :updatedBy, :updatedBy)
            ON CONFLICT (DOCUMENT) DO UPDATE SET
                BIRTHDAY = EXCLUDED.BIRTHDAY,
                EMAIL = EXCLUDED.EMAIL,
                UPDATED_AT = :updatedAt,
                UPDATED_BY = :updatedBy
            RETURNING *
            """, nativeQuery = true)
    PersonEntity upsert(
            @Param("document") String document,
            @Param("birthDay") LocalDate birthDay,
            @Param("email") String email,
            @Param("updatedAt") OffsetDateTime updatedAt,
            @Param("updatedBy") String updatedBy
    );
}