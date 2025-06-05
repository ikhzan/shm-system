package com.ikhzan.shm.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageRouter {
    private final RabbitTemplate rabbitTemplate;

    public MessageRouter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void routeMessageToQueue(String payload) {
        rabbitTemplate.convertAndSend("your-exchange", "your-routing-key", payload);
    }

}
