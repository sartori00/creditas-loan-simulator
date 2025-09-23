package br.com.creditas.loansimulator.infrastructure.gateway.email;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;

public interface MailServiceAdapter {
    void sendEmail(LoanSimulation loanSimulation, String content);
}
