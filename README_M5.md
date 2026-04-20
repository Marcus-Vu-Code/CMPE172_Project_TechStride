# CMPE 172 Term Project – M5 (Distribution Boundary Design)

## What changed for M5

This milestone introduces a **mock remote notification service** that simulates an external confirmation provider.

### Main application boundary endpoint
- `POST /api/appointments/book-and-notify`
- Responsibilities:
  - Validate and book a slot locally using JDBC + transaction logic
  - If the booking succeeds, call the mock external service
  - Return both booking status and notification status in one coarse-grained response

### Mock external service endpoint
- `POST /mock-api/notifications/booking-confirmation`
- Responsibilities:
  - Pretend to queue a booking confirmation notification
  - Return a tracking ID and queue status

## Why this is coarse-grained
The client does not need to call multiple fine-grained remote endpoints such as `createMessage`, `lookupTemplate`, or `sendEmail`. Instead, it sends one business-level request: **book and notify**.

## Example request
```http
POST /api/appointments/book-and-notify
Content-Type: application/json

{
  "candidateId": 1,
  "slotId": 1001,
  "serviceId": 1
}
```

## Example response
```json
{
  "booked": true,
  "candidateId": 1,
  "slotId": 1001,
  "serviceId": 1,
  "notificationSent": true,
  "notificationStatus": "QUEUED",
  "notificationTrackingId": "NOTIFY-1001-...",
  "message": "Booking committed locally and confirmation was queued successfully."
}
```

## Key design decision
The booking transaction is completed **before** the remote call result is interpreted. A notification failure should not roll back a valid appointment because the remote service lives outside the local ACID boundary.
