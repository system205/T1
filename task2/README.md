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