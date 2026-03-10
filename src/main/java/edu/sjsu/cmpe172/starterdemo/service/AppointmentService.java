package edu.sjsu.cmpe172.starterdemo.service;

import edu.sjsu.cmpe172.starterdemo.model.Appointment;
import edu.sjsu.cmpe172.starterdemo.model.AvailabilitySlot;
import edu.sjsu.cmpe172.starterdemo.repository.AppointmentRepository;
import edu.sjsu.cmpe172.starterdemo.repository.SlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final SlotRepository slotRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, SlotRepository slotRepository) {
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
    }

    public List<AvailabilitySlot> listAvailableSlots() {
        return slotRepository.findAvailableSlots();
    }

    public List<Appointment> listAppointments() {
        return appointmentRepository.findAll();
    }

    /**
     * M3 skeleton booking flow (basic).
     * M4 will expand this into a proper double-booking prevention strategy.
     */
    @Transactional
    public boolean book(long candidateId, long slotId, long serviceId) {
        Optional<AvailabilitySlot> slot = slotRepository.findById(slotId);
        if (slot.isEmpty() || !"Available".equalsIgnoreCase(slot.get().getStatus())) {
            return false;
        }

        // Insert appointment record
        appointmentRepository.insert(candidateId, slotId, serviceId);

        // Mark slot as booked
        slotRepository.markBooked(slotId);
        return true;
    }
}
