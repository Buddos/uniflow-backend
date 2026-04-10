# DOC Log

## 2026-04-10
- Action: Implemented Module I cohort-based projection rule for timetable capacity.
- Status: Updated
- Changes: Added `totalAdmittedStudents` and `registeredStudents` to timetable entries and schema, refactored timetable 110% capacity validation to use admitted cohort counts only, and updated timetable output mapping to report admitted cohort size. Added unit tests proving acceptance at >=110% admitted and rejection below 110% even when registered count is zero.
- Files: [src/main/java/com/uniflow/model/TimetableEntry.java](src/main/java/com/uniflow/model/TimetableEntry.java), [src/main/java/com/uniflow/service/TimetableService.java](src/main/java/com/uniflow/service/TimetableService.java), [src/main/java/com/uniflow/controller/TimetableController.java](src/main/java/com/uniflow/controller/TimetableController.java), [uniflow-schema.sql](uniflow-schema.sql), [src/test/java/com/uniflow/service/TimetableServiceTest.java](src/test/java/com/uniflow/service/TimetableServiceTest.java)
- Commit: Not created
- Push: Not pushed

- Action: Implemented digital voucher QR generation for equipment pickup.
- Status: Updated
- Changes: Added ZXing dependencies, created a QR code generator service that returns Base64 PNG output, added a booking voucher endpoint at `/api/bookings/{id}/voucher`, and introduced unit tests for QR generation validity and payload validation.
- Files: [pom.xml](pom.xml), [src/main/java/com/uniflow/service/QRCodeGeneratorService.java](src/main/java/com/uniflow/service/QRCodeGeneratorService.java), [src/main/java/com/uniflow/controller/BookingController.java](src/main/java/com/uniflow/controller/BookingController.java), [src/main/java/com/uniflow/service/BookingService.java](src/main/java/com/uniflow/service/BookingService.java), [src/test/java/com/uniflow/service/QRCodeGeneratorServiceTest.java](src/test/java/com/uniflow/service/QRCodeGeneratorServiceTest.java)
- Commit: Not created
- Push: Not pushed

- Action: Implemented Module IV 300m Asset-Home Rule for venue assignment.
- Status: Updated
- Changes: Added `equipmentOfficeName` and `distanceFromOfficeMeters` to the venue model, updated schema, enforced proximity checks in booking assignment with `ProximityViolationException`, prioritized <=300m venues in availability/optimal venue queries, and seeded proximity values in data initialization. Added unit tests for the booking distance guard.
- Files: [src/main/java/com/uniflow/model/Venue.java](src/main/java/com/uniflow/model/Venue.java), [src/main/java/com/uniflow/service/BookingService.java](src/main/java/com/uniflow/service/BookingService.java), [src/main/java/com/uniflow/service/VenueService.java](src/main/java/com/uniflow/service/VenueService.java), [src/main/java/com/uniflow/exception/ProximityViolationException.java](src/main/java/com/uniflow/exception/ProximityViolationException.java), [src/main/java/com/uniflow/exception/GlobalExceptionHandler.java](src/main/java/com/uniflow/exception/GlobalExceptionHandler.java), [src/main/java/com/uniflow/config/DataInitializer.java](src/main/java/com/uniflow/config/DataInitializer.java), [uniflow-schema.sql](uniflow-schema.sql), [src/test/java/com/uniflow/service/BookingServiceTest.java](src/test/java/com/uniflow/service/BookingServiceTest.java)
- Commit: Not created
- Push: Not pushed

- Action: Cleaned working directory before finalizing the system lock change.
- Status: Updated
- Changes: Removed generated runtime log noise from versioned changes and trimmed unused imports from the new unit test to avoid dead boilerplate.
- Files: [logs/uniflow.log](logs/uniflow.log), [src/test/java/com/uniflow/service/RequestServiceTest.java](src/test/java/com/uniflow/service/RequestServiceTest.java)
- Commit: Not created
- Push: Not pushed

- Action: Removed hardcoded datasource credentials from application config.
- Status: Updated
- Changes: Replaced the concrete datasource username and password defaults with `DB_USERNAME` and `DB_PASSWORD` environment variable placeholders in `application.yaml`, and updated the README with local PowerShell setup instructions.
- Files: [src/main/resources/application.yaml](src/main/resources/application.yaml), [README.md](README.md)
- Commit: Not created
- Push: Not pushed

- Action: Enforced the request-submission system lock in the shared request service.
- Status: Updated
- Changes: Added a guard in `RequestService.createRequest(...)` that checks for pending incoming course unit requests for the submitting department and aborts with `IllegalStateException` when any are found. Added a regression test to verify the lock blocks submission and still allows normal saves when no pending requests exist.
- Files: [src/main/java/com/uniflow/service/RequestService.java](src/main/java/com/uniflow/service/RequestService.java), [src/test/java/com/uniflow/service/RequestServiceTest.java](src/test/java/com/uniflow/service/RequestServiceTest.java)
- Commit: Not created
- Push: Not pushed
