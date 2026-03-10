package edu.sjsu.cmpe172.starterdemo.repository;

import edu.sjsu.cmpe172.starterdemo.model.AvailabilitySlot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class SlotRepository {

    private final JdbcTemplate jdbcTemplate;

    public SlotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AvailabilitySlot> findAvailableSlots() {
        String sql = """
                SELECT slot_id, coach_id, start_time, end_time, status
                FROM availability_slots
                WHERE status = 'Available'
                ORDER BY start_time ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            LocalDateTime start = toLdt(rs.getTimestamp("start_time"));
            LocalDateTime end = toLdt(rs.getTimestamp("end_time"));
            return new AvailabilitySlot(
                    rs.getLong("slot_id"),
                    rs.getLong("coach_id"),
                    start,
                    end,
                    rs.getString("status")
            );
        });
    }

    public Optional<AvailabilitySlot> findById(long slotId) {
        String sql = """
                SELECT slot_id, coach_id, start_time, end_time, status
                FROM availability_slots
                WHERE slot_id = ?
                """;

        List<AvailabilitySlot> rows = jdbcTemplate.query(sql, (rs, rowNum) -> {
            LocalDateTime start = toLdt(rs.getTimestamp("start_time"));
            LocalDateTime end = toLdt(rs.getTimestamp("end_time"));
            return new AvailabilitySlot(
                    rs.getLong("slot_id"),
                    rs.getLong("coach_id"),
                    start,
                    end,
                    rs.getString("status")
            );
        }, slotId);

        return rows.stream().findFirst();
    }

    public int markBooked(long slotId) {
        String sql = "UPDATE availability_slots SET status = 'Booked' WHERE slot_id = ?";
        return jdbcTemplate.update(sql, slotId);
    }

    private static LocalDateTime toLdt(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }
}
