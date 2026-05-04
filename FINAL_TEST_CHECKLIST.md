# TechStride Final Project Test Checklist

Use this checklist after running the app locally.

## 1. Start the application

- [ ] Open the project in IntelliJ or VS Code.
- [ ] Make sure Java 17 is selected.
- [ ] Run `StarterDemoApplication` or run `./mvnw spring-boot:run` from the project root.
- [ ] Confirm the console shows the Spring Boot app started on port 8080.
- [ ] Confirm the SQLite schema and seed data load on startup.

## 2. Nice UI / web pages

- [ ] Open `http://localhost:8080/`.
- [ ] Confirm the styled TechStride home page loads.
- [ ] Click `Browse slots` or open `http://localhost:8080/slots`.
- [ ] Confirm the styled table shows available slots.
- [ ] Click `Book` from the slot list.
- [ ] Confirm the booking form opens and preselects the chosen slot.

## 3. Booking flow

- [ ] On `/appointments/book`, use `candidateId=1`, `serviceId=1`, and select an available slot.
- [ ] Submit the form.
- [ ] Confirm the confirmation page shows success.
- [ ] Confirm notification status is `QUEUED` and a tracking ID is displayed.
- [ ] Open `/api/appointments` and confirm a new appointment row exists.
- [ ] Open `/slots` and confirm the booked slot no longer appears.

## 4. Double-booking prevention

Option A: Browser demo

- [ ] Restart the app so seed data resets.
- [ ] Open two browser windows to `/appointments/book`.
- [ ] In both windows, choose the same slot, for example `1001`.
- [ ] Submit the first booking.
- [ ] Submit the second booking.
- [ ] Confirm the first succeeds and the second says the slot is no longer available.

Option B: API demo

- [ ] Restart the app so seed data resets.
- [ ] Run the first command:

```bash
curl -i -X POST http://localhost:8080/api/appointments/book-and-notify \
  -H "Content-Type: application/json" \
  -d '{"candidateId":1,"slotId":1001,"serviceId":1}'
```

- [ ] Run the second command using the same `slotId`:

```bash
curl -i -X POST http://localhost:8080/api/appointments/book-and-notify \
  -H "Content-Type: application/json" \
  -d '{"candidateId":2,"slotId":1001,"serviceId":1}'
```

- [ ] Confirm the first response is successful.
- [ ] Confirm the second response is HTTP `409 Conflict`.
- [ ] Confirm console logs show a booking conflict warning.

## 5. Mock remote notification service

- [ ] Book a new slot using the UI or API.
- [ ] Confirm the response includes `notificationSent=true`.
- [ ] Confirm `notificationStatus=QUEUED`.
- [ ] Confirm the console log shows `Mock notification queued`.
- [ ] Confirm the console log shows `Remote notification accepted`.

## 6. Logging and observability

- [ ] Confirm successful bookings log an INFO message.
- [ ] Confirm double-booking attempts log a WARN message.
- [ ] Confirm mock notification calls log INFO messages.
- [ ] Open `http://localhost:8080/health`.
- [ ] Confirm JSON shows `status: UP`, `database: UP`, and `notificationBaseUrlConfigured: true`.
- [ ] Open `http://localhost:8080/metrics`.
- [ ] Confirm JSON shows `successfulBookings`, `failedBookings`, `totalBookingsObserved`, and `averageBookingLatencyMs`.

## 7. Final submission checks

- [ ] Push the final source code to GitHub.
- [ ] Copy the final report text into Google Docs and export as PDF.
- [ ] Record the 5-10 minute demo video.
- [ ] Upload the video as unlisted or submit according to Canvas instructions.
- [ ] Submit GitHub repo link, final report PDF, and video link.
