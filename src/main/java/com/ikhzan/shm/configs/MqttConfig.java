package com.ikhzan.shm.configs;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    private static final Logger logger = LoggerFactory.getLogger(MqttConfig.class);
    @Value("${mqtt.username}")
    private String mqttUsername;

    @Value("${mqtt.password}")
    private String mqttPassword;

    @Value("${mqtt.broker}")
    private String mqttBroker;


    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient client = new MqttClient(mqttBroker, "client90");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(mqttUsername);
        options.setPassword(mqttPassword.toCharArray());
        client.connect(options);
        if (client.isConnected()){
            logger.info("Connected to TTN");
        }else{
            logger.warn("NOT Connected to TTN");
        }
        return client;
    }

}
