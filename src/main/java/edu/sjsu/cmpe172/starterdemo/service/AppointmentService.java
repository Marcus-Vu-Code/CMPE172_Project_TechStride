package edu.sjsu.cmpe172.starterdemo.service;

import edu.sjsu.cmpe172.starterdemo.model.Appointment;
import edu.sjsu.cmpe172.starterdemo.model.AvailabilitySlot;
import edu.sjsu.cmpe172.starterdemo.repository.AppointmentRepository;
import edu.sjsu.cmpe172.starterdemo.repository.SlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    private static final int MAX_RETRIES = 3;

    private final AppointmentRepository appointmentRepository;
    private final SlotRepository slotRepository;
    private final TransactionTemplate transactionTemplate;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              SlotRepository slotRepository,
                              PlatformTransactionManager transactionManager) {
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
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
        int retries = MAX_RETRIES;
        while (retries > 0) {
            Boolean booked = transactionTemplate.execute(status -> bookSingleAttempt(candidateId, slotId, serviceId));
            if (Boolean.TRUE.equals(booked)) {
                return true;
            }

            retries--;
            if (retries == 0) {
                return false;
            }
        }

        return false;
    }

    private boolean bookSingleAttempt(long candidateId, long slotId, long serviceId) {
        Optional<AvailabilitySlot> slot = slotRepository.findById(slotId);
        if (slot.isEmpty() || !"Available".equalsIgnoreCase(slot.get().getStatus())) {
            return false;
        }

        int updated = slotRepository.reserveIfVersionMatches(slotId, slot.get().getVersion());
        if (updated == 0) {
            return false;
        }

        // Insert only after slot reservation wins, so rollback keeps data consistent.
        appointmentRepository.insert(candidateId, slotId, serviceId);
        return true;
    }
}
