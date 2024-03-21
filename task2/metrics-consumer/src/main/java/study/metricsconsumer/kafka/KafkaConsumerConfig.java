package study.metricsconsumer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import study.metricsconsumer.entity.Metric;

import java.util.Map;

/**
 * Configuration to connect to Kafka broker as a consumer
 * */
@EnableKafka
@Configuration
@Slf4j
public class KafkaConsumerConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Bean
    public ConsumerFactory<String, Metric> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
            JsonDeserializer.TYPE_MAPPINGS, "study.metricsproducer.entity.Metric:study.metricsconsumer.entity.Metric",
            JsonDeserializer.TRUSTED_PACKAGES, "study.*"
        ));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Metric> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, Metric> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
