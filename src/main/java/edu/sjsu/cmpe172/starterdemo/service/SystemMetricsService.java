package edu.sjsu.cmpe172.starterdemo.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class SystemMetricsService {
    private final AtomicInteger successfulBookings = new AtomicInteger(0);
    private final AtomicInteger failedBookings = new AtomicInteger(0);
    private final AtomicLong totalBookingLatencyMs = new AtomicLong(0);

    public void recordBookingSuccess(long latencyMs) {
        successfulBookings.incrementAndGet();
        totalBookingLatencyMs.addAndGet(Math.max(latencyMs, 0));
    }

    public void recordBookingFailure(long latencyMs) {
        failedBookings.incrementAndGet();
        totalBookingLatencyMs.addAndGet(Math.max(latencyMs, 0));
    }

    public Map<String, Object> snapshot() {
        int successes = successfulBookings.get();
        int failures = failedBookings.get();
        int total = successes + failures;
        long averageLatency = total == 0 ? 0 : totalBookingLatencyMs.get() / total;

        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("timestamp", LocalDateTime.now().toString());
        metrics.put("successfulBookings", successes);
        metrics.put("failedBookings", failures);
        metrics.put("totalBookingsObserved", total);
        metrics.put("averageBookingLatencyMs", averageLatency);
        return metrics;
    }
}
