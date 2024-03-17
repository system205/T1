package study.metricsproducer.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import study.metricsproducer.entity.Metric;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Getter
public class MetricsService {
    private final Map<String, Metric> metrics = new ConcurrentHashMap<>();

    public Metric getMetric(String tag) {
        return metrics.get(tag);
    }

    public Metric getAllMetrics() {
        return new Metric("all", metrics.values());
    }

    public void saveMetric(Metric metric) {
        metrics.merge(metric.tag(), metric, (o, n) -> o);
    }
}
