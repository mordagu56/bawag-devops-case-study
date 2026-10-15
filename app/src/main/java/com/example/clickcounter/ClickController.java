package com.example.clickcounter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ClickController {

    private final Counter clickCounter;
    private final String podName = System.getenv().getOrDefault("HOSTNAME", "local");

    public ClickController(MeterRegistry registry) {
        this.clickCounter = Counter.builder("clicks")
                .description("Total number of button clicks")
                .register(registry);
    }

    @PostMapping("/click")
    public Map<String, Object> click() {
        clickCounter.increment();
        return currentState();
    }

    @GetMapping("/clicks")
    public Map<String, Object> getClicks() {
        return currentState();
    }

    private Map<String, Object> currentState() {
        return Map.of(
                "clicks", (long) clickCounter.count(),
                "pod", podName
        );
    }
}