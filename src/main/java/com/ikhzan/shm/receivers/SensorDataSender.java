package com.ikhzan.shm.receivers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SensorDataSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendSensorData(String message) {
        rabbitTemplate.convertAndSend("sensor-data-queue", message);
    }

}
