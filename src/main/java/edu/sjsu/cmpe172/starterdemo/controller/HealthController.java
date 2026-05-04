package edu.sjsu.cmpe172.starterdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {
    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    private final JdbcTemplate jdbcTemplate;
    private final String applicationName;
    private final String notificationBaseUrl;

    public HealthController(JdbcTemplate jdbcTemplate,
                            @Value("${spring.application.name:techstride-scheduler}") String applicationName,
                            @Value("${integration.notification.base-url:}") String notificationBaseUrl) {
        this.jdbcTemplate = jdbcTemplate;
        this.applicationName = applicationName;
        this.notificationBaseUrl = notificationBaseUrl;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", applicationName);
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("notificationBaseUrlConfigured", notificationBaseUrl != null && !notificationBaseUrl.isBlank());

        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            response.put("database", "UP");

            if (notificationBaseUrl != null && !notificationBaseUrl.isBlank()) {
                response.put("status", "UP");
                log.info("Health check passed status=UP database=UP");
                return ResponseEntity.ok(response);
            }

            response.put("status", "DEGRADED");
            log.warn("Health check degraded status=DEGRADED database=UP notificationService=UNCONFIGURED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("database", "DOWN");
            log.error("Health check failed because database query could not complete", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }
}
