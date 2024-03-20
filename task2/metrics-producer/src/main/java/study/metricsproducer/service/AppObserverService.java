package study.metricsproducer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import study.metricsproducer.entity.Metric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppObserverService {
    private final KafkaTemplate<String, Metric> kafkaTemplate;
    private final MetricsEndpoint metricsEndpoint;
    private final List<String> observedMetrics = List.of("jvm.memory.used", "process.cpu.usage");

    @Scheduled(cron = "*/5 * * * * *")
    public void observeApp() {
        log.info("Start metrics collection");
        List<Metric> metrics = new ArrayList<>();

        observedMetrics.forEach(metric -> metrics.add(
            new Metric(metric, metricsEndpoint.metric(metric, Collections.emptyList()).getMeasurements().get(0).getValue())
        ));

        metrics.forEach(this::sendMetric);
    }

    public void sendMetric(Metric metric) {
        kafkaTemplate.send("metrics-topic", metric)
            .whenComplete((result, err) -> {
                if (err != null) throw new IllegalStateException(err);
                final var sentMetric = result.getProducerRecord().value();
                log.info("Metric {}:{} is successfully sent to Kafka", sentMetric.tag(), sentMetric.value());
            });
    }
}
