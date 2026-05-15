# Project Status

Last updated: 2026-05-15

## Completed Features

### Backend (~72%)

| Area | Status |
|------|--------|
| All 10 entities with full CRUD | ✅ Complete |
| Global exception handling | ✅ Complete |
| Jakarta Validation on all DTOs | ✅ Complete |
| Exam planning algorithm (basic) | ✅ Complete |
| Invigilator assignment algorithm | ✅ Complete |
| JWT Authentication | ⚠️ Configured (secret key in properties), not implemented |
| PDF generation | ❌ Not started |
| Frontend | ❌ Not started |

### API Endpoints (57 total)

| Resource | Endpoints | Status |
|----------|-----------|--------|
| Faculty | POST, GET all, GET by id, PUT, DELETE | ✅ 5 |
| Department | POST, GET all, GET by faculty, GET by id, PUT, DELETE | ✅ 6 |
| Classroom | POST, GET all, GET by id, GET available, PUT, DELETE | ✅ 6 |
| Instructor | POST, GET all, GET by id, GET by dept, PUT, DELETE | ✅ 6 |
| Student | POST, GET all, GET by id, GET by no, GET by dept, PUT, DELETE | ✅ 7 |
| Course | POST, GET all, GET by id, GET by code, GET by instructor, GET by dept, GET by semester, PUT, DELETE | ✅ 9 |
| Exam | POST, GET all, GET by id, GET by course, GET by classroom, GET by date, PUT, DELETE | ✅ 8 |
| ExamAssignment | POST, GET all, GET by id, GET by exam, GET by student, GET by classroom, DELETE | ✅ 7 |
| InvigilatorAssignment | POST, GET all, GET by id, GET by exam, GET by instructor, GET by classroom, DELETE | ✅ 7 |
| User | POST, GET all, GET by id, GET by role, DELETE | ✅ 5 |
| Exam Planning | POST /plan/{examId} | ✅ 1 |

**Total: 67 endpoints**

---

## Next Steps (Priority Order)

### High Priority

**1. JWT Authentication** — Security is fully open (`anyRequest().permitAll()`)

Files to create:
- `config/JwtService.java` — generate, validate, extract claims from tokens
- `config/JwtAuthFilter.java` — extends `OncePerRequestFilter`, validates Bearer tokens
- `service/UserDetailsServiceImpl.java` — loads `User` by username for Spring Security
- `controller/AuthController.java` — `POST /api/auth/login`, `POST /api/auth/register`
- Update `SecurityConfig.java` — wire filter chain with role-based `authorizeHttpRequests`

Estimated effort: ~3 hours

**2. User DTO Layer** — `UserController` currently exposes raw `User` entity including `passwordHash`

Files to create:
- `dto/UserCreateRequest.java` — `username`, `password` (plain), `role`
- `dto/UserResponse.java` — `userId`, `username`, `role`, `createdAt` (no hash)
- Update `UserController.java` — use DTOs, add `@Valid`
- Update `UserService.java` — adapt to accept/return DTOs

Estimated effort: ~30 minutes

**3. PDF Generation** — Required for exam schedule printouts and invigilator sign-in sheets

Add dependency to `build.gradle`:
```groovy
implementation 'com.itextpdf:itextpdf:5.5.13.3'
```

Files to create:
- `service/PdfGenerationService.java`
  - `generateExamSchedule(Long examId)` — full seating plan PDF
  - `generateInvigilatorSheet(Long examId)` — invigilator sign-in sheet PDF
- `controller/PdfController.java` — `GET /api/admin/pdf/exam/{examId}/schedule`

Estimated effort: ~2–3 hours

### Medium Priority

**4. Frontend Interface**

Suggested pages:
- `login.html` — authenticate and receive JWT token
- `admin/dashboard.html` — overview statistics
- `admin/exams.html` — create/manage exams, trigger planning
- `student/schedule.html` — student looks up their own exam seat
- `instructor/duties.html` — instructor views their invigilation schedule

Technology: plain HTML/CSS + Vanilla JS with `fetch()` against the REST API.

Estimated effort: ~12–15 hours

**5. Advanced Planning Features**

- Auto-schedule: find optimal date/time automatically (`ExamPlanningService.autoScheduleExam`)
- Conflict detection report (`ExamPlanningService.detectConflicts`)
- Workload rebalancing (`ExamPlanningService.rebalanceInvigilators`)

### Low Priority

**6. Bulk Import**
- CSV upload for mass student enrollment
- Excel import for course lists

**7. Notifications**
- Email to students with seat assignments
- Email to instructors with invigilation schedule

**8. Exam Results**
- Grade entry by instructors
- Grade report generation

---

## Known Issues

| Issue | Severity | Fix |
|-------|----------|-----|
| All endpoints are publicly accessible (`permitAll`) | High | Implement JWT auth |
| `UserController` returns raw `User` entity with `passwordHash` in response | High | Add UserResponse DTO |
| `StudentService.updateStudent` line 112–113 duplicates `setStudentNo` call | Low | Remove duplicate line |

---

## Database Schema

Tables created automatically by Hibernate (`ddl-auto=update`):

```
users              → id, username, password_hash, role, created_at
faculties          → faculty_id, faculty_name, created_at
departments        → department_id, department_name, faculty_id, created_at
classrooms         → classroom_id, campus, building, room_name, capacity, is_available, technical_features, created_at
instructors        → instructor_id, staff_no, full_name, email, department_id, is_available_for_invigilation, duty_count, user_id, created_at
students           → student_id, student_no, tc_no, full_name, faculty_id, department_id, user_id, created_at
courses            → course_id, course_code, course_name, instructor_id, department_id, credit_hours, semester, created_at
exams              → exam_id, exam_name, exam_type, exam_date, exam_time, duration, course_id, classroom_id, is_common_exam, created_at
exam_assignments   → assignment_id, exam_id, student_id, classroom_id, seat_number, created_at
                     UNIQUE(exam_id, student_id)
invigilator_assignments → invigilation_id, exam_id, instructor_id, classroom_id, created_at
                          UNIQUE(exam_id, instructor_id)
```

---

## Time Estimate to Completion

| Feature | Estimate |
|---------|----------|
| JWT Authentication | 3 hours |
| User DTO layer | 30 minutes |
| PDF Generation | 3 hours |
| Frontend (all pages) | 14 hours |
| Advanced planning features | 4 hours |
| CSV/Excel import | 3 hours |
| Email notifications | 2 hours |

**Total remaining: ~30 hours**
