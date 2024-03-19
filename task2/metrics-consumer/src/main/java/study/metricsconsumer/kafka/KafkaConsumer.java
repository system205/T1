package study.metricsconsumer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import study.metricsconsumer.entity.Metric;

@Component
@Slf4j
public class KafkaConsumer {
    @KafkaListener(topics = "metrics-topic", groupId = "metrics-consumer")
    public void listenMetricsTopic(Metric metric){
        log.info("New metric consumed: {}", metric);
    }
}
