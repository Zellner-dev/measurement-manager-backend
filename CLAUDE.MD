# Workout Manager

REST API for managing workouts, exercises, workout logs and body measurements.

## Stack

- Java 25, Spring Boot 4.1, Maven (with Maven Wrapper)
- Spring Web MVC, Spring Data JPA (Hibernate), Bean Validation
- Spring Security (HTTP Basic, users from the `users` table), springdoc-openapi (Swagger UI)
- PostgreSQL in production; in-memory H2 (PostgreSQL mode) for local development and tests
- Flyway for schema migrations
- No Lombok: write getters/setters explicitly

## Commands

```bash
./mvnw spring-boot:run   # run the app (http://localhost:8080)
./mvnw test              # run tests
./mvnw compile           # quick compile check
./mvnw package           # compile + test + package
```

On Windows use `mvnw.cmd` or `./mvnw` from Git Bash.

Swagger UI: http://localhost:8080/swagger-ui.html (use "Authorize" with email + password).

## Profiles and configuration

- Default profile: H2 in memory, H2 console enabled, Swagger enabled.
- `prod` profile (`SPRING_PROFILES_ACTIVE=prod`): PostgreSQL, H2 console and Swagger disabled.
  Required env vars: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `CORS_ALLOWED_ORIGINS`. Optional: `SWAGGER_ENABLED`.
- `compose.yaml` starts a local PostgreSQL to test the `prod` profile.
- Never commit credentials; read them from environment variables.

## Project structure

```
src/main/java/com/zellner/workoutmanager/
├── config/       # Security, OpenAPI and other @Configuration classes
├── controller/   # @RestController — HTTP layer only
├── dto/          # request/response records
├── exception/    # custom exceptions + @RestControllerAdvice
├── service/      # @Service — business rules, ownership checks, @Transactional
├── repository/   # Spring Data JpaRepository interfaces
└── entity/       # JPA @Entity classes
src/main/resources/db/migration/  # Flyway migrations (V1__..., V2__...)
docs/
├── database.erd.json        # ERD (erd-editor format) — source of truth for the schema
└── commit-emoji-pattern.md  # commit message emojis
```

## Architecture rules

- Layering is strictly `controller → service → repository`. Controllers never call repositories directly.
- Controllers receive and return DTOs (Java `record`s), never entities. Validate input DTOs with `@Valid` and Bean Validation annotations (`@NotBlank`, `@Email`, `@Positive`...).
- Business logic and transactions live in services (`@Transactional` on write methods, `@Transactional(readOnly = true)` on reads).
- Use constructor injection (no `@Autowired` on fields).
- Return proper HTTP status codes (`201 Created` on create, `204 No Content` on delete, `404` when not found). Handle errors in a `@RestControllerAdvice`.
- Never store plain-text passwords: `passwordHash` must hold a BCrypt (or stronger) hash.
- Users can only access their own data. Every service `findById` must call `userService.checkOwner(...)`, and
  other methods must load resources through `findById`. Resources owned by another user return `404`, not `403`.
- Never expose endpoints that list data from all users.

## Entity conventions

- Table names are plural snake_case: `@Table(name = "workouts")`.
- Primary key: `@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;`
- Column names are snake_case and explicit: `@Column(name = "password_hash", nullable = false, length = 255)`.
- Set `nullable` and `length` to match the ERD.
- Relationships: `@ManyToOne(fetch = FetchType.LAZY, optional = false)` + `@JoinColumn(name = "<parent>_id", nullable = false)`. Avoid bidirectional `@OneToMany` unless really needed; prefer repository queries like `findByOwnerId(Long ownerId)`.
- Avoid SQL reserved words as column names (`value`, `date`, `user`, `order`...). Use descriptive names: `measured_value`, `measured_at`, `performed_at`, `weight`.
- Date/time: `LocalDateTime` for `TIMESTAMP`, `LocalDate` for `DATE`. Suffix timestamps with `_at`.

## Data model

| Table | Columns | References |
|---|---|---|
| `users` | id, email (unique), name, password_hash | — |
| `workouts` | id, name, owner_id | users |
| `exercises` | id, name, workout_id | workouts |
| `workout_logs` | id, performed_at, workout_id | workouts |
| `exercise_logs` | id, weight, workout_log_id, exercise_id | workout_logs, exercises |
| `measurements` | id, name, measured_value, measured_at, owner_id | users |

All ids and foreign keys are `BIGINT` (`Long`).

## Database migrations

- The schema is owned by Flyway (`src/main/resources/db/migration`); Hibernate runs with `ddl-auto=validate`.
- Any entity change needs a new migration file (`V<n>__<description>.sql`). Never edit a migration that was already applied.
- Migrations must run on both PostgreSQL and H2 (PostgreSQL mode).
- FKs use `ON DELETE CASCADE`, mirrored by `@OnDelete(action = OnDeleteAction.CASCADE)` on the entities.

## ERD

- `docs/database.erd.json` must stay in sync with the entities. When changing an entity, update the ERD too (and vice versa).
- The file is also edited in the erd-editor (VS Code extension). If it is open there, saving from the editor overwrites external changes, so reload it before editing the JSON directly.

## Code style

- 4 spaces, no tabs; max line length 120.
- Imports ordered `java`, `javax`, `jakarta`, `org`, `com`; no unused or wildcard imports.
- Omit annotation attributes that only repeat the default when writing new code.

## Commits

Format: `<emoji> <type>: <short description in English>`, following `docs/commit-emoji-pattern.md`. Examples:

```
✨ feat: add workout CRUD endpoints
🐛 fix: return 404 when workout is not found
📚 docs: update database ERD
🎨 style: reformat entities
🔨 refactor: extract workout mapper
✅ test: add WorkoutService tests
```

Keep unrelated changes in separate commits (e.g. code vs. ERD vs. formatting).
