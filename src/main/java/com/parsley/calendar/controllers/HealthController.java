package com.parsley.calendar.controllers;

import com.parsley.calendar.models.Health;
import org.springframework.web.bind.annotation.GetMapping;

public class HealthController {

    @GetMapping("/health")
    public Health getHealth() {
        return Health.green();
    }
}
