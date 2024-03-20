package study.metricsconsumer.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import study.metricsconsumer.entity.Metric;
import study.metricsconsumer.services.MetricsService;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final MetricsService service;
    @KafkaListener(topics = "metrics-topic", groupId = "metrics-consumer")
    public void listenMetricsTopic(Metric metric, @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp){
        metric.setTimestamp(Instant.ofEpochMilli(timestamp));
        log.info("New metric consumed: {}", metric);

        service.saveMetric(metric);
    }
}
