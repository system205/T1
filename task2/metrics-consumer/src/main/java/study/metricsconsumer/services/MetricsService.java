package study.metricsconsumer.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import study.metricsconsumer.entity.Metric;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;

@Service
@Getter
public class MetricsService {
    private final Map<String, List<Metric>> metrics = new ConcurrentHashMap<>();

    /**
     * @param tag  tag or name of the metric
     * @param from timestamp to return measurements from
     * @param to   timestamp to return measurement up to
     * @return measurements for the metric within the time range
     */
    public Iterable<Metric> getMetricMeasurements(String tag, Instant from, Instant to) {
        List<Metric> timedMetrics = new ArrayList<>();
        if (!metrics.containsKey(tag)) {
            return Collections.emptyList();
        }
        for (Metric metric : metrics.get(tag)) {
            if (metric.getTimestamp().isBefore(from) || metric.getTimestamp().isAfter(to)) continue;
            timedMetrics.add(metric);
        }
        return timedMetrics;
    }

    /**
     * @return last measurements for all metrics
     */
    public Iterable<Metric> getAllMetrics() {
        List<Metric> lastMetrics = new ArrayList<>();
        for (var m : metrics.entrySet()) {
            lastMetrics.add(m.getValue().get(m.getValue().size() - 1));
        }
        return lastMetrics;
    }

    /**
     * Saves a metric measurement
     */
    public void saveMetric(Metric metric) {
        List<Metric> initList = new ArrayList<>(1);
        initList.add(metric);
        metrics.merge(metric.getTag(), initList, (o, n) -> {
            o.addAll(n);
            return o;
        });
    }

    public DoubleSummaryStatistics getMetricStatistics(Iterable<Metric> measurements) {
        return StreamSupport.stream(measurements.spliterator(), true)
            .mapToDouble(Metric::getValue)
            .summaryStatistics();
    }

    public boolean isMetricExists(String tag) {
        return metrics.containsKey(tag);
    }
}
