package study.metricsproducer.endpoints;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import study.metricsproducer.entity.Metric;
import study.metricsproducer.services.MetricsService;

@WebEndpoint(id = "custom-metrics")
@Component("customMetricsEndpoint")
@RequiredArgsConstructor
public class MetricsEndpoint {
    private final MetricsService service;
    @ReadOperation
    public Metric getMetric(@Nullable String tag){
        if (tag == null) {
            return service.getAllMetrics();
        }
        return service.getMetric(tag);
    }

}
