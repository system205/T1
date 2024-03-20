package study.metricsproducer.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.metricsproducer.entity.Metric;
import study.metricsproducer.service.AppObserverService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
@Slf4j
public class MetricsController {
    private final AppObserverService service;
    @PostMapping
    public ResponseEntity<Void> test(@RequestBody List<Metric> metrics) {
        metrics.forEach(service::sendMetric);
        return ResponseEntity.accepted().build();
    }
}
