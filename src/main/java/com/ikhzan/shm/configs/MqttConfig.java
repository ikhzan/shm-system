package com.ikhzan.shm.configs;

import io.github.cdimascio.dotenv.Dotenv;
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

    private final Dotenv dotenv = Dotenv.load();

    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient client = new MqttClient(dotenv.get("MQTT_BROKER"), "client90");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(dotenv.get("MQTT_USERNAME"));
        options.setPassword(dotenv.get("MQTT_PASSWORD").toCharArray());
        client.connect(options);
        if (client.isConnected()){
            logger.info("Connected to TTN");
        }else{
            logger.warn("NOT Connected to TTN");
        }
        return client;
    }

}
