package study.metricsconsumer.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import study.metricsconsumer.entity.Metric;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Getter
public class MetricsService {
    private final Map<String, List<Metric>> metrics = new ConcurrentHashMap<>();

    public Iterable<Metric> getMetricMeasurements(String tag, Instant from, Instant to) {
        List<Metric> timedMetrics = new ArrayList<>();
        for (Metric metric : metrics.get(tag)) {
            if (metric.getTimestamp().isBefore(from) || metric.getTimestamp().isAfter(to)) continue;
            timedMetrics.add(metric);
        }
        return timedMetrics;
    }

    /**
     * @return last measurements for all metrics
     * */
    public Iterable<Metric> getAllMetrics() {
        List<Metric> lastMetrics = new ArrayList<>();
        for (var m : metrics.entrySet()) {
            lastMetrics.add(m.getValue().get(m.getValue().size()-1));
        }
        return lastMetrics;
    }

    /**
     * Saves a metric measurement
     * */
    public void saveMetric(Metric metric) {
        List<Metric> initList = new ArrayList<>(1);
        initList.add(metric);
        metrics.merge(metric.getTag(), initList, (o, n) -> {
            o.addAll(n);
            return o;
        });
    }


}
