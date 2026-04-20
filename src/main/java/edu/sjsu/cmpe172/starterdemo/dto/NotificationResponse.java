package edu.sjsu.cmpe172.starterdemo.dto;

public class NotificationResponse {
    private boolean delivered;
    private String status;
    private String trackingId;
    private String detail;

    public NotificationResponse() {
    }

    public NotificationResponse(boolean delivered, String status, String trackingId, String detail) {
        this.delivered = delivered;
        this.status = status;
        this.trackingId = trackingId;
        this.detail = detail;
    }

    public static NotificationResponse failure(String detail) {
        return new NotificationResponse(false, "FAILED", null, detail);
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
