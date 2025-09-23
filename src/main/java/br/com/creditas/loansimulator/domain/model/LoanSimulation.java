package br.com.creditas.loansimulator.domain.model;

import br.com.creditas.loansimulator.domain.model.enums.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LoanSimulation {
    private final Currency currency;

    private final BigDecimal loanAmount;

    private final int qtInstallments;

    private final Person person;

    private BigDecimal installmentAmount;

    private BigDecimal loanAmountBRL;

    public LoanSimulation(Currency currency, BigDecimal loanAmount, int qtInstallments, Person person) {
        this.currency = currency;
        this.loanAmount = loanAmount;
        this.qtInstallments = qtInstallments;
        this.person = person;
    }


    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public int getQtInstallments() {
        return qtInstallments;
    }

    public Person getPerson() {
        return person;
    }

    public BigDecimal getTotalAmountToPay() {
        return this.installmentAmount.multiply(new BigDecimal(this.qtInstallments))
                .setScale(2, RoundingMode.UP);
    }

    public BigDecimal getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(BigDecimal installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public BigDecimal getTotalInterest() {
        return this.getTotalAmountToPay()
                .subtract(this.loanAmountBRL)
                .setScale(2, RoundingMode.UP);
    }

    public BigDecimal getLoanAmountBRL() {
        return this.loanAmountBRL;
    }

    public void setLoanAmountBRL(BigDecimal loanAmountBRL) {
        this.loanAmountBRL = loanAmountBRL;
    }
}
