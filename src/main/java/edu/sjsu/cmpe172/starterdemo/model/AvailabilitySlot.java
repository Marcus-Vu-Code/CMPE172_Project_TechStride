package edu.sjsu.cmpe172.starterdemo.model;

import java.time.LocalDateTime;

public class AvailabilitySlot {
    private final long slotId;
    private final long coachId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String status;

    public AvailabilitySlot(long slotId, long coachId, LocalDateTime startTime, LocalDateTime endTime, String status) {
        this.slotId = slotId;
        this.coachId = coachId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public long getSlotId() { return slotId; }
    public long getCoachId() { return coachId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getStatus() { return status; }
}
