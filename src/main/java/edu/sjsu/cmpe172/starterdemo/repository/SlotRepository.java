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
                SELECT slot_id, coach_id, start_time, end_time, status, version
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
                    rs.getString("status"),
                    rs.getInt("version")
            );
        });
    }

    public Optional<AvailabilitySlot> findById(long slotId) {
        String sql = """
                SELECT slot_id, coach_id, start_time, end_time, status, version
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
                    rs.getString("status"),
                    rs.getInt("version")
            );
        }, slotId);

        return rows.stream().findFirst();
    }

    public int reserveIfVersionMatches(long slotId, int expectedVersion) {
        String sql = """
                UPDATE availability_slots
                SET status = 'Booked', version = version + 1
                WHERE slot_id = ?
                  AND status = 'Available'
                  AND version = ?
                """;
        return jdbcTemplate.update(sql, slotId, expectedVersion);
    }

    private static LocalDateTime toLdt(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }
}
