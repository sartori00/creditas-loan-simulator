package br.com.creditas.loansimulator.infrastructure.gateway.producer.notification;

import br.com.creditas.loansimulator.infrastructure.gateway.producer.notification.impl.dto.EmailNotificationDTO;

public interface EmailNotificationProducer {
    void sendToEmailNotification(EmailNotificationDTO dto);
}
