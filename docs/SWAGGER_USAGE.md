# Swagger / OpenAPI 3 — Usage Guide

## Accessing the UI

Start the application, then open:

```
http://localhost:8080/swagger-ui.html
```

The raw OpenAPI JSON spec is at:

```
http://localhost:8080/v3/api-docs
```

---

## Authentication Flow

All `/api/admin/**` endpoints require a JWT Bearer token.

### Step 1 — Register or Login

**POST** `/api/auth/register`
```json
{
  "username": "admin",
  "password": "secret123",
  "role": "ADMIN"
}
```

Or **POST** `/api/auth/login`
```json
{
  "username": "admin",
  "password": "secret123"
}
```

Both return an `AuthResponse`:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "role": "ADMIN",
  "expiresIn": 86400000
}
```

### Step 2 — Authorize in Swagger UI

1. Click the **Authorize** button (lock icon) at the top right of the Swagger UI
2. In the **Bearer Authentication** field, paste your token (without `Bearer ` prefix)
3. Click **Authorize** then **Close**

All subsequent requests will include the `Authorization: Bearer <token>` header automatically.

---

## API Groups

The UI is organized into tagged groups:

| Group | Tag | Base Path |
|-------|-----|-----------|
| Authentication | Authentication | `/api/auth` |
| Faculties | Faculties | `/api/admin/faculties` |
| Departments | Departments | `/api/admin/departments` |
| Instructors | Instructors | `/api/admin/instructors` |
| Students | Students | `/api/admin/students` |
| Courses | Courses | `/api/admin/courses` |
| Classrooms | Classrooms | `/api/admin/classrooms` |
| Exams | Exams | `/api/admin/exams` |
| Exam Assignments | Exam Assignments | `/api/admin/exam-assignments` |
| Invigilator Assignments | Invigilator Assignments | `/api/admin/invigilator-assignments` |
| Exam Planning | Exam Planning | `/api/admin/exam-planning` |
| Users | Users | `/api/admin/users` |

Use the **group selector** dropdown at the top to filter by group.

---

## Exam Planning Endpoint

**POST** `/api/admin/exam-planning/plan/{examId}`

Provide an exam ID in the path and a JSON array of student IDs in the body:

```json
[1, 2, 3, 4, 5, 10, 15, 20]
```

The algorithm will:
1. Sort available classrooms by capacity (largest first)
2. Fill classrooms sequentially, assigning seat numbers
3. Assign invigilators per room based on duty count (fewest first):
   - 1–50 students → 1 invigilator
   - 51–100 students → 2 invigilators
   - 101+ students → 3 invigilators

Returns a summary map with classroom breakdowns and invigilator assignments.

---

## Tips

- Use the **filter bar** (top of page) to search endpoints by keyword
- Models are collapsed by default — click a schema name to expand
- All date fields use ISO 8601 format: `2026-06-15` for dates, `09:00` for times
- Token expiry is 24 hours (86400000 ms) — re-login when expired
