package com.momo.task.manager.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.ArrayList;

@Configuration
@EnableJms
public class JmsConfig {

    ArrayList<String> clientList = new ArrayList<>();
    @Value("${activemq.broker-url}")
    private String brokerUrl;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        containerFactory.setPubSubDomain(true);
        containerFactory.setConnectionFactory(connectionFactory());
        containerFactory.setMessageConverter(jacksonJmsMsgConverter());

//        Durability applies only to messages within a topic, not within a queue.
//        In the case of a durable subscription, ActiveMQ will retain messages if the subscriber is unavailable.
//        When that subscriber reconnects, it receives new messages that arrived during the time it was disconnected.
//        A non-durable subscriber would not receive any messages published to the topic during the time it was disconnected
//        from the broker.
        containerFactory.setSubscriptionDurable(true);
        return containerFactory;
    }


    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        ActiveMQConnectionFactory activeMQConnFactory = new ActiveMQConnectionFactory();
        activeMQConnFactory.setBrokerURL(brokerUrl);
        factory.setTargetConnectionFactory(activeMQConnFactory);
        factory.setClientId("Client_73");
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMsgConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

}
