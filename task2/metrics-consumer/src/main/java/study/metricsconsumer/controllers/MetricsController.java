package study.metricsconsumer.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.metricsconsumer.entity.Metric;

import java.util.List;

@RestController
@RequestMapping("/metrics")
public class MetricsController {
    public ResponseEntity<Iterable<Metric>> getMetrics(){
        return ResponseEntity.ok(List.of());
    }
}
