package com.momo.task.manager.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AdminAddedEventListener {
    @JmsListener(destination = "${emp.jms.topic.admin-added}", containerFactory = "jmsListenerContainerFactory")
    public void handleAdminAddedEvent() {
        log.info("Received admin added event: ");
    }
}
