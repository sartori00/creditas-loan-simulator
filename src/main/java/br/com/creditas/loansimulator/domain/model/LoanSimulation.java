package br.com.creditas.loansimulator.domain.model;

import br.com.creditas.loansimulator.domain.model.enums.Currency;

import java.math.BigDecimal;

public class LoanSimulation {
    private final Currency currency;

    private final BigDecimal loanAmount;

    private final int qtInstallments;

    private final Person person;

    private BigDecimal totalAmountToPay;

    private BigDecimal installmentAmount;

    private BigDecimal totalInterest;

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
        return totalAmountToPay;
    }

    public void setTotalAmountToPay(BigDecimal totalAmountToPay) {
        this.totalAmountToPay = totalAmountToPay;
    }

    public BigDecimal getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(BigDecimal installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }
}
