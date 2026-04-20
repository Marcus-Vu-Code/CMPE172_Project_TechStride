package edu.sjsu.cmpe172.starterdemo.dto;

public class BookingRequest {
    private long candidateId;
    private long slotId;
    private long serviceId;

    public BookingRequest() {
    }

    public BookingRequest(long candidateId, long slotId, long serviceId) {
        this.candidateId = candidateId;
        this.slotId = slotId;
        this.serviceId = serviceId;
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
}
