package com.ikhzan.shm.repository;

import com.ikhzan.shm.data.DeviceData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceDataRepository extends MongoRepository<DeviceData, String> {
}
