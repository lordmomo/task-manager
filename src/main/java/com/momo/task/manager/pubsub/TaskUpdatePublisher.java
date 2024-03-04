package com.momo.task.manager.pubsub;

import com.momo.task.manager.dto.TaskUpdateEventDto;
import jakarta.jms.*;
import org.springframework.stereotype.Component;


@Component
public class TaskUpdatePublisher {

//    public void publishTaskUpdateEvent(TaskUpdateEventDto taskUpdateEvent) {
//        try (Connection connection = createConnection();
//             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
//
//            // Publish to Project-specific Topic
//            String topic = "TaskUpdatesProject_" + taskUpdateEvent.getProjectId();
//            publishToTopic(session, taskUpdateEvent, topic);
//
//            // Publish to Admins (optional)
//            if (taskUpdateEvent.isAdmin()) {
//                publishToTopic(session, taskUpdateEvent, "TaskUpdatesAdmin");
//            }
//
//        } catch (JMSException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void publishToTopic(Session session, TaskUpdateEventDto taskUpdateEvent, String topicName) throws JMSException {
//        try (MessageProducer producer = session.createProducer(createTopic(session, topicName))) {
//            ObjectMessage message = session.createObjectMessage();
//            message.setObject(taskUpdateEvent);
//            producer.send(message);
//        }
//    }
////    private Connection createConnection() throws JMSException {
////        PooledConnectionFactory connectionFactory = new PooledConnectionFactory();
////        connectionFactory.setBrokerURL(ActiveMQConfig.BROKER_URL);
////        return connectionFactory.createConnection();
////    }
//
//    private Topic createTopic(Session session, String topicName) throws JMSException {
//        return session.createTopic(topicName);
//    }
}
