# Monitoring System

## Tech Stacks
1. Spring boot
2. NoSQL mongodb community
3. RabbitMQ as message broker
4. Redis server to provide message cache

## Features
1. Sensor should be able to register based on name and type
2. after obtain token, sensor should be able to add data
3. User can access data of sensors
4. server should be to process message broker data for each sensor

## logs
- check in folder logs in the project, it is not modified yet to make dynamic so we can see where ever we assign the path

## End points
1. check swagger http://localhost:8080/swagger-ui/index.html
2. sensor api
   - http://localhost:8080/api/sensors/obtain-token --> no need to send with token
   - http://localhost:8080/api/sensors/{sensor-name}/add-data --> send with authorization token
   - 