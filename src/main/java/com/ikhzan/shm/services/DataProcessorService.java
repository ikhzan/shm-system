package com.ikhzan.shm.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikhzan.shm.data.DeviceData;
import com.ikhzan.shm.data.UplinkData;
import com.ikhzan.shm.repository.DeviceDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessorService.class);
    private final DeviceDataRepository deviceDataRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public DataProcessorService(DeviceDataRepository deviceDataRepository) {
        this.deviceDataRepository = deviceDataRepository;
        this.objectMapper = new ObjectMapper();
    }

    public void processPayload(String payload) {
        try {
            UplinkData uplinkData = objectMapper.readValue(payload, UplinkData.class);
            logger.info("Decoded Uplink Data: {}", uplinkData);
            saveData(uplinkData);
        } catch (Exception e) {
            logger.error("Failed to process payload", e);
        }
    }

    private void saveData(UplinkData uplinkData) {
        DeviceData deviceData = new DeviceData();
        deviceData.setDeviceId(uplinkData.getEndDeviceIds().get("device_id").toString());
        deviceData.setPayload(uplinkData.getUplinkMessage().getDecodedPayload().toString());
        deviceData.setReceivedAt(uplinkData.getReceivedAt());

        deviceDataRepository.save(deviceData);
        logger.info("Data saved to MongoDB for device: {}", deviceData.getDeviceId());
    }
}
