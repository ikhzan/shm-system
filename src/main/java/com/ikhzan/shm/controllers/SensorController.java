package com.ikhzan.shm.controllers;

import com.ikhzan.shm.data.Sensor;
import com.ikhzan.shm.data.SensorData;
import com.ikhzan.shm.receivers.SensorDataSender;
import com.ikhzan.shm.services.SensorService;
import com.ikhzan.shm.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SensorDataSender sensorDataSender;

    private static final Logger logger = LoggerFactory.getLogger(SensorController.class);


    @PostMapping("/obtain-token")
    public ResponseEntity<?> obtainToken(@RequestParam String name, @RequestParam String sensorType) {
        // Check if the sensor exists
        logger.info("POST-request {}",name);
        Sensor sensor = sensorService.findByName(name);
        if (sensor != null) {
            // Sensor is already registered; generate a new token
            String token = JwtUtil.generateToken(name);
            return ResponseEntity.ok().body(Map.of(
                    "message", "Sensor is already registered. New token generated successfully.",
                    "token", token
            ));
        }

        // If the sensor doesn't exist, create it
        sensor = new Sensor(name);
        sensor.setSensorType(sensorType);
        sensorService.saveSensorData(sensor); // Save the new sensor

        // Generate a token for the new sensor
        String newToken = JwtUtil.generateToken(name);
        return ResponseEntity.ok().body(Map.of(
                "message", "Sensor was not registered. New sensor created and token generated successfully.",
                "token", newToken
        ));

    }

    @PostMapping("/refresh-sensor-token")
    public ResponseEntity<?> refreshSensorToken(@RequestBody String sensorId) {
        try {
            String accessToken = JwtUtil.generateRefreshToken(sensorId);
            return ResponseEntity.ok(accessToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }


    @PostMapping("/send-data")
    public String sendDataToQueue(@RequestBody String message) {
        try {
            logger.info("send message {}",message);
            sensorDataSender.sendSensorData(message);
            return "Message sent to queue: " + message;
        }catch (Exception ex){
            return "NONE";
        }
    }
    

    // Endpoint to add data to a sensor
    @PostMapping("/{name}/add-data")
    public ResponseEntity<?> addSensorData(@PathVariable String name, @RequestBody SensorData sensorData) {
        // Find the sensor by name
        Sensor sensor = sensorService.findByName(name);
        if (sensor == null) {
            // Return a 404 Not Found response if the sensor doesn't exist
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Sensor not found",
                    "details", "The sensor with name '" + name + "' does not exist in the system."
            ));
        }

        // Add data to the sensor's data list
        sensor.getDataList().add(sensorData);
        sensorService.saveSensorData(sensor); // Save the updated sensor

        // Return a success response
        return ResponseEntity.ok(Map.of(
                "message", "Sensor data added successfully",
                "sensorName", name,
                "newData", sensorData
        ));
    }


    @PostMapping
    public ResponseEntity<Sensor> createSensorData(@RequestBody Sensor sensorData){
        Sensor createData = sensorService.saveSensorData(sensorData);

        return ResponseEntity.ok(createData);
    }

    @GetMapping
    public ResponseEntity<List<Sensor>> getAllSensorData(){
        List<Sensor> sensorDataList = sensorService.getAllSensorData();
        return ResponseEntity.ok(sensorDataList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable String id){
        Sensor sensorData = sensorService.getSensorDataById(id);
        if (sensorData == null){
            return ResponseEntity.ok(sensorData);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sensor> updateSensorData(@PathVariable String id, @RequestBody Sensor sensorData){
        Sensor existingData = sensorService.getSensorDataById(id);
        if (existingData != null){
            sensorData.setId(id);
            Sensor updateData = sensorService.saveSensorData(sensorData);
            return ResponseEntity.ok(updateData);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensorData(@PathVariable String id){
        Sensor existingData = sensorService.getSensorDataById(id);
        if (existingData != null) {
            sensorService.deleteSensorData(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/predict")
    public ResponseEntity<?> getPrediction(@RequestBody Map<String, Object> sensorData) {
        try {
            logger.info("Sending request to Python API: {}", sensorData);
            // Python ML API URL
            String pythonApiUrl = "http://localhost:5000/predict";
            Map<String, Object> response = restTemplate.postForObject(pythonApiUrl, sensorData, Map.class);

            // Check if Python API returned an error
            if (response != null && response.containsKey("error")) {
                return ResponseEntity.badRequest().body(response); // Return the Python error to the client
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to get prediction from the Python API"));
        }
    }

}
