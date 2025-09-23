package br.com.creditas.loansimulator.infrastructure.gateway.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.OffsetDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditEntity {

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private OffsetDateTime updatedAt;

    @CreatedBy
    @Column(name = "CREATED_BY", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "UPDATED_BY")
    private String updatedBy;
}