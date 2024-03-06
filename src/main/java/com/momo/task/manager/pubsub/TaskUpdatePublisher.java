package com.momo.task.manager.pubsub;

import com.momo.task.manager.dto.TaskUpdateEventDto;
import jakarta.jms.JMSException;
import jakarta.jms.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class TaskUpdatePublisher {

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${emp.jms.topic.task-updated}")
    private String taskUpdateMessenger;

    public void publishTaskUpdateEvent(TaskUpdateEventDto taskUpdateEvent) {
        try {
            Topic taskUpdateTopic = jmsTemplate.getConnectionFactory()
                    .createConnection()
                    .createSession()
                    .createTopic(taskUpdateMessenger);
            jmsTemplate.convertAndSend(taskUpdateTopic, taskUpdateEvent);
        } catch (NullPointerException | JMSException e) {
            e.printStackTrace();
        }
    }
}
