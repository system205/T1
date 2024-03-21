package study.metricsconsumer.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @ApiResponse(responseCode = "200", description = "Last measurements for all metrics",
        content = @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Metric.class))
        ))
    @GetMapping
    public ResponseEntity<Iterable<Metric>> getMetrics() {
        return ResponseEntity.ok(service.getAllMetrics());
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Measurements for the metric",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Metric.class))
            )),
        @ApiResponse(responseCode = "404", description = "Metric not found", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Iterable<Metric>> getMetric(@Parameter(description = "tag or name of the metric")
                                                      @PathVariable("id") String tag,
                                                      @Parameter(description = "timestamp to return measurements from")
                                                      @RequestParam(defaultValue = "1") Long from,
                                                      @Parameter(description = "timestamp to return measurement up to")
                                                      @RequestParam(defaultValue = "99999999999999") Long to) {
        if (!service.isMetricExists(tag))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(service.getMetricMeasurements(tag,
            Instant.ofEpochMilli(from),
            Instant.ofEpochMilli(to)
        ));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Some statistics for the metric",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = DoubleSummaryStatistics.class)
            )),
        @ApiResponse(responseCode = "404", description = "Metric not found", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @GetMapping("/{id}/stats")
    public ResponseEntity<DoubleSummaryStatistics> getMetricStatistics(@Parameter(description = "tag or name of the metric")
                                                                       @PathVariable("id") String tag,
                                                                       @Parameter(description = "timestamp to return measurements from")
                                                                       @RequestParam(defaultValue = "1") Long from,
                                                                       @Parameter(description = "timestamp to return measurement up to")
                                                                       @RequestParam(defaultValue = "99999999999999") Long to) {
        if (!service.isMetricExists(tag))
            return ResponseEntity.notFound().build();

        final Iterable<Metric> metricMeasurements = service.getMetricMeasurements(tag,
            Instant.ofEpochMilli(from),
            Instant.ofEpochMilli(to)
        );
        final DoubleSummaryStatistics metricStatistics = service.getMetricStatistics(metricMeasurements);

        log.info("Retrieved statistics for metric {} => {} ", tag, metricStatistics);
        return ResponseEntity.ok(metricStatistics);
    }
}
