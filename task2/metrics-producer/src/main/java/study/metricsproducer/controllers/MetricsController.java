package study.metricsproducer.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.metricsproducer.entity.Metric;
import study.metricsproducer.services.MetricsService;

import java.util.List;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
@Slf4j
public class MetricsController {
    private final KafkaTemplate<String, Metric> kafkaTemplate;
    private final MetricsService service;

    @PostMapping
    public ResponseEntity<Void> test(@RequestBody List<Metric> metrics) {
        for (Metric metric : metrics) {
            service.saveMetric(metric);
            kafkaTemplate.send("metrics-topic", metric)
                .thenAccept(result -> {
                    final var sentMetric = result.getProducerRecord().value();
                    log.info("Metric {}:{} is successfully sent to Kafka", sentMetric.tag(), sentMetric.value());
                });
        }
        return ResponseEntity.accepted().build();
    }
}
