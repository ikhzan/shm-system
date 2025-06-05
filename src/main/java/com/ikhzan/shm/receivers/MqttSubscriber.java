package com.ikhzan.shm.receivers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikhzan.shm.data.DeviceData;
import com.ikhzan.shm.data.UplinkData;
import com.ikhzan.shm.repository.DeviceDataRepository;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqttSubscriber {

    @Autowired
    private DeviceDataRepository repository;

    private final MqttClient mqttClient;
    private static final Logger logger = LoggerFactory.getLogger(MqttSubscriber.class);


    public MqttSubscriber(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
        subscribeToDevices();
    }

    public void subscribeToDevices() {
        String[] deviceTopics = {
                "v3/humidity-sensor@zaim-university/devices/eui-a84041e2b1829f76/up",
                "v3/humidity-sensor@zaim-university/devices/eui-a840418d81829f8a/up",
                "v3/humidity-sensor@zaim-university/devices/lora-2/up",
                "v3/humidity-sensor@zaim-university/devices/lora-3/up",
                "v3/humidity-sensor@zaim-university/devices/lora-1/up"
        };

        for (String topic : deviceTopics) {
            try {
                mqttClient.subscribe(topic, (receivedTopic, message) -> {
                    String payload = new String(message.getPayload());
                    logger.info("Received from " + receivedTopic + ": {}" , payload);
                    processMessage(receivedTopic, payload);
                });
            } catch (MqttException e) {
                logger.error("Error {}",e.getMessage());
            }
        }
    }

    private void processPayload(String payload) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            UplinkData uplinkData = objectMapper.readValue(payload, UplinkData.class);
            logger.info("Decoded Uplink Data: {}", uplinkData);
            saveToDatabase(uplinkData); // Handle the uplink data
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse JSON payload: {}", e.getMessage());
        }
    }


    private void processMessage(String topic, String payload) {
        if (topic.contains("eui-a84041e2b1829f76")) {
            logger.info("Processing data for Device 1");
            processPayload(payload);
        } else if (topic.contains("eui-a840418d81829f8a")) {
            logger.info("Processing data for Device 2");
            processPayload(payload);
        } else if (topic.contains("lora-2")) {
            logger.info("Processing data for Lora 2");
            processPayload(payload);
        } else if (topic.contains("lora-3")) {
            logger.info("Processing data for Lora 3");
            processPayload(payload);
        } else if (topic.contains("lora-1")) {
            logger.info("Processing data for Lora 1");
            processPayload(payload);
        } else {
            logger.warn("Unknown device in topic: {}", topic);
        }
    }

    private void saveToDatabase(UplinkData uplinkData) {
        // Save to MongoDB using the repository
        DeviceData deviceData = new DeviceData();
        deviceData.setDeviceId(uplinkData.getEndDeviceIds().get("device_id").toString());
        deviceData.setPayload(uplinkData.getUplinkMessage().getDecodedPayload().toString());
        deviceData.setReceivedAt(uplinkData.getReceivedAt());

        repository.save(deviceData);
        logger.info("Saved data to MongoDB for Device ID: {}", deviceData.getDeviceId());
    }

}
