package study.metricsconsumer.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.metricsconsumer.entity.Metric;
import study.metricsconsumer.services.MetricsService;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
public class MetricsController {
    private final MetricsService service;

    @GetMapping
    public ResponseEntity<Iterable<Metric>> getMetrics() {
        return ResponseEntity.ok(service.getAllMetrics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Iterable<Metric>> getMetric(@PathVariable("id") String tag,
                                                      @RequestParam(defaultValue = "1") Long from,
                                                      @RequestParam(defaultValue = "99999999999999") Long to) {
        return ResponseEntity.ok(service.getMetricMeasurements(tag,
            Instant.ofEpochMilli(from),
            Instant.ofEpochMilli(to)
        ));
    }
}
