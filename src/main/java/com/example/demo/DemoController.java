package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DemoController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/greet")
    public Map<String, String> greet(
            @RequestParam(defaultValue = "Mundo") String name) {

        return Map.of(
                "message",
                "Hola " + name
        );
    }

    @GetMapping("/sum")
    public Map<String, Integer> sum(
            @RequestParam int a,
            @RequestParam int b) {

        return Map.of(
                "result",
                a + b
        );
    }
}