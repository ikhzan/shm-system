package com.ikhzan.shm.configs;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    private static final Logger logger = LoggerFactory.getLogger(MqttConfig.class);

    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient client = new MqttClient("tcp://eu1.cloud.thethings.industries:1883", "client90");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("humidity-sensor@zaim-university");
        options.setPassword("NNSXS.MODEQU4P7GKVJJE6VGCPIH52EU5NAQHMLZCUHNI.CH2OYV67UC4MDSZTSGWGEGFYOBHYEBYVIZNWROLBIXWJLZEJMJ7Q".toCharArray());
        client.connect(options);
        if (client.isConnected()){
            logger.info("Connected to TTN");
        }else{
            logger.warn("NOT Connected to TTN");
        }
        return client;
    }

}
