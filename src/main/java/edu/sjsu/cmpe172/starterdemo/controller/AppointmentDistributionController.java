package edu.sjsu.cmpe172.starterdemo.controller;

import edu.sjsu.cmpe172.starterdemo.dto.BookingRequest;
import edu.sjsu.cmpe172.starterdemo.dto.BookingResponse;
import edu.sjsu.cmpe172.starterdemo.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentDistributionController {

    private final AppointmentService appointmentService;

    public AppointmentDistributionController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/book-and-notify")
    public ResponseEntity<BookingResponse> bookAndNotify(@RequestBody BookingRequest request) {
        BookingResponse response = appointmentService.bookAndNotify(
                request.getCandidateId(),
                request.getSlotId(),
                request.getServiceId()
        );

        if (!response.isBooked()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if (!response.isNotificationSent()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
