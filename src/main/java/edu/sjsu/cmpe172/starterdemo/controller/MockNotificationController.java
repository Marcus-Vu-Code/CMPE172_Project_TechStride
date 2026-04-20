package edu.sjsu.cmpe172.starterdemo.controller;

import edu.sjsu.cmpe172.starterdemo.dto.NotificationRequest;
import edu.sjsu.cmpe172.starterdemo.dto.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock-api/notifications")
public class MockNotificationController {
    private static final Logger log = LoggerFactory.getLogger(MockNotificationController.class);

    @PostMapping("/booking-confirmation")
    public ResponseEntity<NotificationResponse> acceptBookingConfirmation(@RequestBody NotificationRequest request) {
        String trackingId = "NOTIFY-" + request.getSlotId() + "-" + System.currentTimeMillis();
        log.info("Mock notification queued eventType={} candidateId={} slotId={} channel={} trackingId={}",
                request.getEventType(), request.getCandidateId(), request.getSlotId(),
                request.getDeliveryChannel(), trackingId);

        NotificationResponse response = new NotificationResponse(
                true,
                "QUEUED",
                trackingId,
                "Mock notification accepted for asynchronous delivery."
        );

        return ResponseEntity.accepted().body(response);
    }
}
