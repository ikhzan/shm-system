package com.ikhzan.shm.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue sensorDataQueue() {
        return new Queue("sensor-data-queue", true); // durable queue
    }

}
