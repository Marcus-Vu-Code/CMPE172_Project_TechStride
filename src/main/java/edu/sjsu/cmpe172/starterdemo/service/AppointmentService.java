package edu.sjsu.cmpe172.starterdemo.service;

import edu.sjsu.cmpe172.starterdemo.dto.BookingResponse;
import edu.sjsu.cmpe172.starterdemo.dto.NotificationRequest;
import edu.sjsu.cmpe172.starterdemo.dto.NotificationResponse;
import edu.sjsu.cmpe172.starterdemo.integration.NotificationGateway;
import edu.sjsu.cmpe172.starterdemo.model.Appointment;
import edu.sjsu.cmpe172.starterdemo.model.AvailabilitySlot;
import edu.sjsu.cmpe172.starterdemo.repository.AppointmentRepository;
import edu.sjsu.cmpe172.starterdemo.repository.SlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);
    private static final int MAX_RETRIES = 3;

    private final AppointmentRepository appointmentRepository;
    private final SlotRepository slotRepository;
    private final NotificationGateway notificationGateway;
    private final TransactionTemplate transactionTemplate;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              SlotRepository slotRepository,
                              NotificationGateway notificationGateway,
                              PlatformTransactionManager transactionManager) {
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
        this.notificationGateway = notificationGateway;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
    }

    public List<AvailabilitySlot> listAvailableSlots() {
        return slotRepository.findAvailableSlots();
    }

    public List<Appointment> listAppointments() {
        return appointmentRepository.findAll();
    }

    public boolean book(long candidateId, long slotId, long serviceId) {
        return attemptBookingWithRetry(candidateId, slotId, serviceId);
    }

    public BookingResponse bookAndNotify(long candidateId, long slotId, long serviceId) {
        boolean booked = attemptBookingWithRetry(candidateId, slotId, serviceId);
        if (!booked) {
            return BookingResponse.bookingConflict(candidateId, slotId, serviceId);
        }

        NotificationRequest notificationRequest = new NotificationRequest(
                "BOOKING_CONFIRMED",
                candidateId,
                slotId,
                serviceId,
                "EMAIL",
                "Your TechStride appointment has been booked successfully."
        );

        NotificationResponse notificationResponse = notificationGateway.sendBookingConfirmation(notificationRequest);
        boolean sent = notificationResponse.isDelivered();
        String message = sent
                ? "Booking committed locally and confirmation was queued successfully."
                : "Booking committed locally, but the remote notification call failed. Retry can be handled separately.";

        return new BookingResponse(
                true,
                candidateId,
                slotId,
                serviceId,
                sent,
                notificationResponse.getStatus(),
                notificationResponse.getTrackingId(),
                message
        );
    }

    private boolean attemptBookingWithRetry(long candidateId, long slotId, long serviceId) {
        int retries = MAX_RETRIES;
        while (retries > 0) {
            try {
                Boolean booked = transactionTemplate.execute(status -> {
                    bookSingleAttempt(candidateId, slotId, serviceId);
                    return true;
                });
                return Boolean.TRUE.equals(booked);
            } catch (ConflictException e) {
                retries--;
                log.warn("Booking conflict for slot {} (candidateId={}). retriesRemaining={}", slotId, candidateId, retries);
                if (retries == 0) {
                    return false;
                }
            }
        }

        return false;
    }

    private void bookSingleAttempt(long candidateId, long slotId, long serviceId) {
        Optional<AvailabilitySlot> slot = slotRepository.findById(slotId);
        if (slot.isEmpty() || !"Available".equalsIgnoreCase(slot.get().getStatus())) {
            throw new ConflictException("Slot is not available for booking");
        }

        int updated = slotRepository.reserveIfVersionMatches(slotId, slot.get().getVersion());
        if (updated == 0) {
            throw new ConflictException("Conflict detected. Slot version mismatch - retry booking.");
        }

        appointmentRepository.insert(candidateId, slotId, serviceId);
        log.info("Booking committed for candidateId={} slotId={} serviceId={}", candidateId, slotId, serviceId);
    }

    private static class ConflictException extends RuntimeException {
        public ConflictException(String message) {
            super(message);
        }
    }
}
