package edu.sjsu.cmpe172.starterdemo.integration;

import tools.jackson.databind.ObjectMapper;
import edu.sjsu.cmpe172.starterdemo.dto.NotificationRequest;
import edu.sjsu.cmpe172.starterdemo.dto.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class NotificationGateway {
    private static final Logger log = LoggerFactory.getLogger(NotificationGateway.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String notificationBaseUrl;

    public NotificationGateway(ObjectMapper objectMapper,
                               @Value("${integration.notification.base-url:http://localhost:8080}") String notificationBaseUrl) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
        this.objectMapper = objectMapper;
        this.notificationBaseUrl = notificationBaseUrl;
    }

    public NotificationResponse sendBookingConfirmation(NotificationRequest payload) {
        try {
            String requestBody = objectMapper.writeValueAsString(payload);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(notificationBaseUrl + "/mock-api/notifications/booking-confirmation"))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(5))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                NotificationResponse notificationResponse = objectMapper.readValue(response.body(), NotificationResponse.class);
                log.info("Remote notification accepted for slot {} with trackingId={}",
                        payload.getSlotId(), notificationResponse.getTrackingId());
                return notificationResponse;
            }

            log.warn("Remote notification service returned status {} for slot {}",
                    response.statusCode(), payload.getSlotId());
            return NotificationResponse.failure("Remote service returned HTTP " + response.statusCode());
        } catch (Exception e) {
            log.error("Remote notification call failed for slot {}", payload.getSlotId(), e);
            return NotificationResponse.failure("Notification service unreachable: " + e.getMessage());
        }
    }
}
