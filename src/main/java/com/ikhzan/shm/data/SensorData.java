package com.ikhzan.shm.data;

import java.time.LocalDateTime;

public class SensorData {
    private double sensorValue;
    private LocalDateTime timeStamp;

    public SensorData(){
        this.timeStamp = LocalDateTime.now();
    }

    public SensorData(double sensorValue) {
        this.sensorValue = sensorValue;
        this.timeStamp = LocalDateTime.now();
    }

    public double getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(double sensorValue) {
        this.sensorValue = sensorValue;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "sensorValue=" + sensorValue +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
