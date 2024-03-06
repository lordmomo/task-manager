package com.momo.task.manager.listener;

import com.momo.task.manager.dto.TaskUpdateEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskUpdateEventListener {

    @JmsListener(destination = "${emp.jms.topic.task-updated}", containerFactory = "jmsListenerContainerFactory")
    public void handleTaskUpdateEvent(TaskUpdateEventDto taskUpdateEvent) {
        log.info("Received task update event: " + taskUpdateEvent);
    }
}
