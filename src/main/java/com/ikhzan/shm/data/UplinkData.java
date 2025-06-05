package com.ikhzan.shm.data;

import java.util.Map;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class UplinkData {

    @JsonProperty("end_device_ids")
    private Map<String, Object> endDeviceIds;

    @JsonProperty("received_at")
    private String receivedAt;

    @JsonProperty("uplink_message")
    private UplinkMessage uplinkMessage;

    // Getters and Setters
    public Map<String, Object> getEndDeviceIds() {
        return endDeviceIds;
    }

    public void setEndDeviceIds(Map<String, Object> endDeviceIds) {
        this.endDeviceIds = endDeviceIds;
    }

    public String getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(String receivedAt) {
        this.receivedAt = receivedAt;
    }

    public UplinkMessage getUplinkMessage() {
        return uplinkMessage;
    }

    public void setUplinkMessage(UplinkMessage uplinkMessage) {
        this.uplinkMessage = uplinkMessage;
    }

    public static class UplinkMessage {
        @JsonProperty("decoded_payload")
        private Map<String, Object> decodedPayload;

        // Getters and Setters
        public Map<String, Object> getDecodedPayload() {
            return decodedPayload;
        }

        public void setDecodedPayload(Map<String, Object> decodedPayload) {
            this.decodedPayload = decodedPayload;
        }
    }
}