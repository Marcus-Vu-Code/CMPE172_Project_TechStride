package edu.sjsu.cmpe172.starterdemo.repository;

import edu.sjsu.cmpe172.starterdemo.model.Appointment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AppointmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppointmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Appointment> findAll() {
        String sql = """
                SELECT appointment_id, candidate_id, slot_id, service_id, booking_date
                FROM appointments
                ORDER BY booking_date DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            LocalDateTime booked = toLdt(rs.getTimestamp("booking_date"));
            return new Appointment(
                    rs.getLong("appointment_id"),
                    rs.getLong("candidate_id"),
                    rs.getLong("slot_id"),
                    rs.getLong("service_id"),
                    booked
            );
        });
    }

    public int insert(long candidateId, long slotId, long serviceId) {
        String sql = """
                INSERT INTO appointments(candidate_id, slot_id, service_id, booking_date)
                VALUES (?, ?, ?, CURRENT_TIMESTAMP)
                """;
        return jdbcTemplate.update(sql, candidateId, slotId, serviceId);
    }

    private static LocalDateTime toLdt(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }
}
