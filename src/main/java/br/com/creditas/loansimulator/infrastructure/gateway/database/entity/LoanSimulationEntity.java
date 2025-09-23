package br.com.creditas.loansimulator.infrastructure.gateway.database.entity;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity(name = "LOAN_SIMULATION")
@EntityListeners(AuditingEntityListener.class)
public class LoanSimulationEntity  extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", updatable = false, unique = true, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private PersonEntity person;

    @Enumerated(EnumType.STRING)
    @Column(name = "CURRENCY")
    private Currency currency;

    @Column(name = "LOAN_AMOUNT")
    private BigDecimal loanAmount;

    @Column(name = "LOAN_AMOUNT_BRL")
    private BigDecimal loanAmountBRL;

    @Column(name = "QT_INSTALLMENTS")
    private int qtInstallments;

    @Column(name = "TOTAL_AMOUNT_TO_PAY")
    private BigDecimal totalAmountToPay;

    @Column(name = "INSTALLMENT_AMOUNT")
    private BigDecimal installmentAmount;

    @Column(name = "TOTAL_INTEREST")
    private BigDecimal totalInterest;

    public LoanSimulationEntity(LoanSimulation loanSimulation){
        this.id = loanSimulation.getId();
        this.person = new PersonEntity(loanSimulation.getPerson());
        this.currency = loanSimulation.getCurrency();
        this.loanAmount = loanSimulation.getLoanAmount();
        this.loanAmountBRL = loanSimulation.getLoanAmountBRL();
        this.qtInstallments = loanSimulation.getQtInstallments();
        this.totalAmountToPay = loanSimulation.getTotalAmountToPay();
        this.installmentAmount = loanSimulation.getInstallmentAmount();
        this.totalInterest = loanSimulation.getTotalInterest();
    }
}
