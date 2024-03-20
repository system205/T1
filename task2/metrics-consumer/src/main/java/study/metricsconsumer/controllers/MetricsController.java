package study.metricsconsumer.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.metricsconsumer.entity.Metric;
import study.metricsconsumer.services.MetricsService;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
public class MetricsController {
    private final MetricsService service;

    @GetMapping
    public ResponseEntity<Iterable<Metric>> getMetrics(){
        return ResponseEntity.ok(service.getAllMetrics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Iterable<Metric>> getMetric(@PathVariable("id") String tag){
        return ResponseEntity.ok(service.getMetricMeasurements(tag));
    }
}
