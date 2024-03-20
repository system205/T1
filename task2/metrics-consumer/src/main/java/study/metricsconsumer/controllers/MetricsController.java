package study.metricsconsumer.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.metricsconsumer.entity.Metric;
import study.metricsconsumer.services.MetricsService;

import java.time.Instant;
import java.util.DoubleSummaryStatistics;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
@Slf4j
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

    @GetMapping("/{id}/stats")
    public ResponseEntity<DoubleSummaryStatistics> getMetricStatistics(@PathVariable("id") String tag,
                                                                       @RequestParam(defaultValue = "1") Long from,
                                                                       @RequestParam(defaultValue = "99999999999999") Long to) {
        final Iterable<Metric> metricMeasurements = service.getMetricMeasurements(tag,
            Instant.ofEpochMilli(from),
            Instant.ofEpochMilli(to)
        );
        final DoubleSummaryStatistics metricStatistics = service.getMetricStatistics(metricMeasurements);

        log.info("Retrieved statistics for metric {} => {} ", tag, metricStatistics);
        return ResponseEntity.ok(metricStatistics);
    }
}
