package br.com.creditas.loansimulator.infrastructure.gateway.email;

public interface MailServiceAdapter {
    void sendEmail(String content, String recipientEmail);
}
