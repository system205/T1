package study.metricsproducer.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.metricsproducer.entity.Metric;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
@Slf4j
public class MetricsController {

    private final KafkaTemplate<String, Metric> kafkaTemplate;

    @GetMapping
    public ResponseEntity<String> test(@RequestParam String metric) {
        log.info("New metric: {}", metric);
        kafkaTemplate.send("metrics-topic", new Metric(metric, metric));
        return ResponseEntity.ok().build();
    }
}
