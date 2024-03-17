package study.metricsproducer.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @GetMapping
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Test is done");
    }
}
