package com.ikhzan.shm.services;

import com.ikhzan.shm.data.Sensor;
import com.ikhzan.shm.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorService {

    @Autowired
    private SensorDataRepository sensorDataRepository;

    public Sensor saveSensorData(Sensor sensorData) {
        return sensorDataRepository.save(sensorData);
    }

    public List<Sensor> getAllSensorData(){
        return sensorDataRepository.findAll();
    }

    public Sensor getSensorDataById(String id){
        return sensorDataRepository.findById(id).orElse(null);
    }

    public void deleteSensorData(String id){
        sensorDataRepository.deleteById(id);
    }

    public Sensor findByName(String name) {
        return sensorDataRepository.findByName(name);
    }
}
