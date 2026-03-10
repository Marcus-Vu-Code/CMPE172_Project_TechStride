package edu.sjsu.cmpe172.starterdemo.model;

import java.time.LocalDateTime;

public class Appointment {
    private final long appointmentId;
    private final long candidateId;
    private final long slotId;
    private final long serviceId;
    private final LocalDateTime bookingDate;

    public Appointment(long appointmentId, long candidateId, long slotId, long serviceId, LocalDateTime bookingDate) {
        this.appointmentId = appointmentId;
        this.candidateId = candidateId;
        this.slotId = slotId;
        this.serviceId = serviceId;
        this.bookingDate = bookingDate;
    }

    public long getAppointmentId() { return appointmentId; }
    public long getCandidateId() { return candidateId; }
    public long getSlotId() { return slotId; }
    public long getServiceId() { return serviceId; }
    public LocalDateTime getBookingDate() { return bookingDate; }
}
