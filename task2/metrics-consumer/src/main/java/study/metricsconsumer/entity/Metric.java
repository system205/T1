package study.metricsconsumer.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class Metric {

    private String tag;
    private Double value;
    private Instant timestamp;
}
