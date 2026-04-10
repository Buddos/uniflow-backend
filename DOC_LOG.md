# DOC Log

## 2026-04-10
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
