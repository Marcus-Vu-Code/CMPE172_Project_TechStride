package edu.sjsu.cmpe172.starterdemo;

import edu.sjsu.cmpe172.starterdemo.service.AppointmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
class StarterDemoApplicationTests {
	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void bookingSameSlotTwiceShouldOnlySucceedOnce() {
		long slotId = 1002L;

		boolean first = appointmentService.book(1L, slotId, 1L);
		boolean second = appointmentService.book(2L, slotId, 1L);

		Integer count = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM appointments WHERE slot_id = ?",
				Integer.class,
				slotId
		);

		String status = jdbcTemplate.queryForObject(
				"SELECT status FROM availability_slots WHERE slot_id = ?",
				String.class,
				slotId
		);

		Assertions.assertTrue(first);
		Assertions.assertFalse(second);
		Assertions.assertEquals(1, count);
		Assertions.assertEquals("Booked", status);
	}

}
