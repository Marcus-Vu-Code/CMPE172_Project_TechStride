package edu.sjsu.cmpe172.starterdemo.dto;

public class BookingResponse {
    private boolean booked;
    private long candidateId;
    private long slotId;
    private long serviceId;
    private boolean notificationSent;
    private String notificationStatus;
    private String notificationTrackingId;
    private String message;

    public BookingResponse() {
    }

    public BookingResponse(boolean booked, long candidateId, long slotId, long serviceId,
                           boolean notificationSent, String notificationStatus,
                           String notificationTrackingId, String message) {
        this.booked = booked;
        this.candidateId = candidateId;
        this.slotId = slotId;
        this.serviceId = serviceId;
        this.notificationSent = notificationSent;
        this.notificationStatus = notificationStatus;
        this.notificationTrackingId = notificationTrackingId;
        this.message = message;
    }

    public static BookingResponse bookingConflict(long candidateId, long slotId, long serviceId) {
        return new BookingResponse(false, candidateId, slotId, serviceId, false,
                "SKIPPED", null, "Slot is no longer available, so no remote notification was sent.");
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(long candidateId) {
        this.candidateId = candidateId;
    }

    public long getSlotId() {
        return slotId;
    }

    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public boolean isNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(boolean notificationSent) {
        this.notificationSent = notificationSent;
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getNotificationTrackingId() {
        return notificationTrackingId;
    }

    public void setNotificationTrackingId(String notificationTrackingId) {
        this.notificationTrackingId = notificationTrackingId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
