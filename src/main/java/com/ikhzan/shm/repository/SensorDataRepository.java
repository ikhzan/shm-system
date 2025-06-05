package com.ikhzan.shm.repository;

import com.ikhzan.shm.data.Sensor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataRepository extends MongoRepository<Sensor,String> {
    Sensor findByName(String name);
}
