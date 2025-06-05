package com.ikhzan.shm.receivers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SensorDataReceiver {

    private static final Logger logger = LoggerFactory.getLogger(SensorDataReceiver.class);

    @RabbitListener(queues = "sensor-data-queue") // Specify the queue to listen on
    public void receiveSensorData(String message) {
        logger.info("Received message: {}", message);
        // Add logic to handle the received sensor data
    }

}
