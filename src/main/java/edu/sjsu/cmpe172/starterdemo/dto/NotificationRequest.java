package edu.sjsu.cmpe172.starterdemo.dto;

public class NotificationRequest {
    private String eventType;
    private long candidateId;
    private long slotId;
    private long serviceId;
    private String deliveryChannel;
    private String message;

    public NotificationRequest() {
    }

    public NotificationRequest(String eventType, long candidateId, long slotId, long serviceId,
                               String deliveryChannel, String message) {
        this.eventType = eventType;
        this.candidateId = candidateId;
        this.slotId = slotId;
        this.serviceId = serviceId;
        this.deliveryChannel = deliveryChannel;
        this.message = message;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    public String getDeliveryChannel() {
        return deliveryChannel;
    }

    public void setDeliveryChannel(String deliveryChannel) {
        this.deliveryChannel = deliveryChannel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
