# Description

- Producer: 
  - It observes and sends app metrics to a Kafka topic 'metrics-topic'
  - Accepts a list of metrics in POST to send them to the Kafka topic 
- Consumer:
  - It listens to the Kafka topic 'metrics-topic', logs and stores the metrics in a cache
  - It provides a REST API to get the metric(s) measurements filtering by timestamp
  - It can produce a summary for a metric
- Single Kafka instance (controller and broker):
  - It is used to communicate between the producer and the consumer
  - It is started with docker compose, and it is available at localhost:9092 (while controller is on localhost:9093)
  - The topic 'metrics-topic' is created automatically when a producer or a consumer will subscribe to it

# Set up

1. Start a Kafka broker on localhost:9092 with 
```bash
docker compose up
```

Make sure you have the following properties in the modules:

[Consumer properties](/task2/metrics-consumer/src/main/resources/application.properties):
- spring.kafka.bootstrap-servers=http://localhost:9092

[Producer properties](/task2/metrics-producer/src/main/resources/application.properties):
- spring.kafka.bootstrap-servers=http://localhost:9092
- server.port=8081
- management.endpoints.web.exposure.include=metrics - this is **necessary** to include the [MetricsEndpoint](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/actuate/metrics/MetricsEndpoint.html) in the actuator.

2. Run the [MetricsProducerApplication](/task2/metrics-producer/src/main/java/com/epam/metrics/MetricsProducerApplication.java) 
and [MetricsConsumerApplication](/task2/metrics-consumer/src/main/java/com/epam/metrics/MetricsConsumerApplication.java) modules with the following commands:
- Producer:
```bash
cd metrics-producer
mvn spring-boot:run
```
- Consumer:
```bash
cd metrics-consumer 
mvn spring-boot:run
```

# Features

## GET
- /metrics **➡** returns last measurement for each metric 
- /metrics/{id} **➡** returns the measurements of the metric with the tag 'id' or 404. 
Optionally you can add 'from' and 'to' query parameters as long values to filter the results by timestamp. 

## POST
- /metrics **➡** take an array of Metric objects **➡** the producer will publish the metrics to the Kafka topic.
  - Body example:
  ```json
  [
    {
      "tag": "process.cpu.usage",
      "value": 67.0
    },
    {
      "tag": "jvm.memory.used",
      "value": 1234E2
    }
  ]
  ```