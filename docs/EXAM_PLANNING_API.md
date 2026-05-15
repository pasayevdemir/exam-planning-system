# Exam Planning API Documentation

## Current Features (Implemented)

### POST /api/admin/exam-planning/plan/{examId}

Automatically assigns students to available classrooms and assigns instructors as invigilators.

**Path parameter:** `examId` — ID of the exam to plan

**Request Body:**
```json
[1, 2, 3, 4, 5]
```
A JSON array of student IDs (Long values).

**Algorithm:**
1. Validates the exam exists
2. Validates all student IDs exist (throws 404 per missing student)
3. Checks each student is not already assigned to this exam
4. Checks each student has no scheduling conflict (another exam at same date/time)
5. Finds all available classrooms (`isAvailable = true`) not already booked at the exam date/time
6. Sorts classrooms by capacity descending (largest rooms fill first)
7. Validates total available capacity >= number of students
8. Sorts students by student number (alphabetical seating)
9. Assigns students room-by-room, seat numbers starting at 1 per room
10. Calculates required invigilators per classroom:
    - 1–50 students: 1 invigilator
    - 51–100 students: 2 invigilators
    - 101+ students: 3 invigilators
11. Selects eligible instructors sorted by `dutyCount` ascending (lowest workload first)
    - Must have `isAvailableForInvigilation = true`
    - Must not already be assigned to this exam
    - Must have no conflicting assignment at the same date/time
12. Creates `InvigilatorAssignment` records and increments each instructor's `dutyCount`
13. Returns full planning summary

**Success Response (200 OK):**
```json
{
  "examId": 1,
  "examName": "BIL513 Midterm Exam",
  "examDate": "2026-06-10",
  "examTime": "09:00:00",
  "totalStudents": 5,
  "classroomsUsed": 1,
  "invigilatorsAssigned": 1,
  "classrooms": [
    {
      "classroom": "Tuzla - Engineering - M201",
      "capacity": 80,
      "studentsAssigned": 5,
      "invigilatorsAssigned": 1,
      "studentNumbers": ["20210001", "20210002", "20210003", "20210004", "20210005"],
      "invigilatorNames": ["Dr. Aysel Mammadova"]
    }
  ]
}
```

**Error Responses:**

| Status | Trigger |
|--------|---------|
| 404 | Exam not found |
| 404 | Any student ID not found |
| 409 | Student already assigned to this exam |
| 409 | Student has a scheduling conflict at same date/time |
| 400 | No available classrooms at the exam date/time |
| 400 | Total classroom capacity insufficient for student count |
| 400 | Not enough available instructors for a classroom |

---

## Future Features (TODO)

### POST /api/admin/exam-planning/auto-schedule/{courseId}

Automatically find the best available date and time for an exam.

**Planned behaviour:**
- Accepts a list of student IDs and a preferred start date
- Searches forward from `preferredDate` for the first slot where:
  - All students are free
  - Sufficient classroom capacity is available
  - Sufficient instructors are available
- Creates the Exam record and runs full planning in one call

**Not yet implemented.** Calling this method currently throws `UnsupportedOperationException`.

---

### GET /api/admin/exam-planning/conflicts/{examId}

Generate a conflict report for an existing exam.

**Planned behaviour:**
- Lists all students double-booked at the same date/time
- Lists all instructors with conflicting invigilation duties
- Lists any classrooms hosting more than one exam simultaneously

**Not yet implemented.**

---

### POST /api/admin/exam-planning/rebalance

Rebalance instructor invigilation workload.

**Planned behaviour:**
- Identifies instructors with duty counts significantly above the group average
- Finds reassignment opportunities in future exams
- Swaps invigilators while ensuring no new conflicts
- Returns a summary of all changes made

**Not yet implemented.**

---

## Testing with Postman — Step-by-Step

### Prerequisites (create in this order)

1. **Create a Faculty**
   `POST /api/admin/faculties`
   ```json
   { "facultyName": "Engineering Faculty" }
   ```
   Save the returned `facultyId`.

2. **Create a Department**
   `POST /api/admin/departments`
   ```json
   { "departmentName": "Computer Engineering", "facultyId": 1 }
   ```

3. **Create a User for Instructor**
   `POST /api/admin/users`
   ```json
   { "username": "dr.mammadova", "passwordHash": "pass123", "role": "INSTRUCTOR" }
   ```

4. **Create an Instructor**
   `POST /api/admin/instructors`
   ```json
   {
     "staffNo": "STF001",
     "fullName": "Dr. Aysel Mammadova",
     "email": "a.mammadova@univ.edu.az",
     "departmentId": 1,
     "isAvailableForInvigilation": true,
     "userId": 1
   }
   ```

5. **Create a Classroom**
   `POST /api/admin/classrooms`
   ```json
   {
     "campus": "Tuzla",
     "building": "Engineering",
     "roomName": "M201",
     "capacity": 80,
     "isAvailable": true
   }
   ```

6. **Create a Course**
   `POST /api/admin/courses`
   ```json
   {
     "courseCode": "BIL513",
     "courseName": "Software Engineering",
     "instructorId": 1,
     "departmentId": 1,
     "creditHours": 3,
     "semester": "Spring 2026"
   }
   ```

7. **Create an Exam**
   `POST /api/admin/exams`
   ```json
   {
     "examName": "BIL513 Midterm Exam",
     "examType": "Midterm",
     "examDate": "2026-06-10",
     "examTime": "09:00:00",
     "duration": 120,
     "courseId": 1,
     "classroomId": 1,
     "isCommonExam": false
   }
   ```

8. **Create Students** (repeat for each student)
   `POST /api/admin/students`
   ```json
   {
     "studentNo": "20210001",
     "tcNo": "12345678901",
     "fullName": "Ali Hasanov",
     "facultyId": 1,
     "departmentId": 1
   }
   ```

### Run Exam Planning

`POST /api/admin/exam-planning/plan/1`

**Headers:** `Content-Type: application/json`

**Body:**
```json
[1, 2, 3, 4, 5]
```

**Expected:** 200 OK with full planning summary.
