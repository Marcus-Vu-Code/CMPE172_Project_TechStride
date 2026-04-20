package edu.sjsu.cmpe172.starterdemo.service;

import edu.sjsu.cmpe172.starterdemo.model.Appointment;
import edu.sjsu.cmpe172.starterdemo.model.AvailabilitySlot;
import edu.sjsu.cmpe172.starterdemo.repository.AppointmentRepository;
import edu.sjsu.cmpe172.starterdemo.repository.SlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    private static final int MAX_RETRIES = 3;

    private final AppointmentRepository appointmentRepository;
    private final SlotRepository slotRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              SlotRepository slotRepository) {
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
    }

    public List<AvailabilitySlot> listAvailableSlots() {
        return slotRepository.findAvailableSlots();
    }

    public List<Appointment> listAppointments() {
        return appointmentRepository.findAll();
    }

    public boolean book(long candidateId, long slotId, long serviceId) {
        int retries = MAX_RETRIES;
        while (retries > 0) {
            try {
                bookSingleAttempt(candidateId, slotId, serviceId);
                return true;
            } catch (ConflictException e) {
                retries--;
                if (retries == 0) {
                    return false;
                }
            }
        }

        return false;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    private void bookSingleAttempt(long candidateId, long slotId, long serviceId) {
        Optional<AvailabilitySlot> slot = slotRepository.findById(slotId);
        if (slot.isEmpty() || !"Available".equalsIgnoreCase(slot.get().getStatus())) {
            throw new ConflictException("Slot is not available for booking");
        }

        int updated = slotRepository.reserveIfVersionMatches(slotId, slot.get().getVersion());
        if (updated == 0) {
            throw new ConflictException("Conflict detected. Slot version mismatch - retry booking.");
        }

        // Insert only after slot reservation wins, so rollback keeps data consistent.
        appointmentRepository.insert(candidateId, slotId, serviceId);
    }

    private static class ConflictException extends RuntimeException {
        public ConflictException(String message) {
            super(message);
        }
    }
}
