package com.ikhzan.shm.data;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;

@Document(collection = "sensors")
public class Sensor {
    @Id
    private String id;
    private String name;
    private String sensorType;
    private List<SensorData> dataList;

    public Sensor(){
        this.dataList = new ArrayList<>();
    }

    public Sensor(String name){
        this.name = name;
    }

    public List<SensorData> getDataList() {
        return dataList;
    }

    public void setDataList(List<SensorData> dataList) {
        this.dataList = dataList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

}
