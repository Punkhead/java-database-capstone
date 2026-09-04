# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

## Admin User Stories

### 1. Admin Login

**Title:**
_As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. The admin can enter a valid username and password.
2. The system authenticates the admin using the provided credentials.
3. Invalid credentials are rejected and an appropriate error message is displayed.

**Priority:** High
**Story Points:** 3
**Notes:**
- Only authorized admins should be able to access the admin portal.

### 2. Admin Logout

**Title:**
_As an admin, I want to log out of the portal, so that I can protect system access._

**Acceptance Criteria:**
1. The admin can log out from the portal.
2. The current authenticated session is invalidated.
3. Protected admin pages cannot be accessed after logout without logging in again.

**Priority:** High
**Story Points:** 2
**Notes:**
- Logout should securely terminate the admin session.

### 3. Add Doctor

**Title:**
_As an admin, I want to add doctors to the portal, so that doctors can use the system and manage patient appointments._

**Acceptance Criteria:**
1. The admin can enter the required doctor information.
2. The system validates the provided doctor information.
3. A valid doctor profile is saved successfully.

**Priority:** High
**Story Points:** 5
**Notes:**
- Required doctor information should be validated before saving.

### 4. Delete Doctor

**Title:**
_As an admin, I want to delete a doctor's profile from the portal, so that inactive doctors no longer have access to the system._

**Acceptance Criteria:**
1. The admin can select a doctor profile.
2. The system asks for confirmation before deletion.
3. The selected doctor's profile is removed successfully.

**Priority:** High
**Story Points:** 3
**Notes:**
- Deletion should be restricted to authorized admins.

### 5. Monthly Appointment Statistics

**Title:**
_As an admin, I want to run a stored procedure in MySQL CLI to get the number of appointments per month, so that I can track usage statistics._

**Acceptance Criteria:**
1. A MySQL stored procedure calculates the number of appointments per month.
2. The admin can execute the stored procedure using MySQL CLI.
3. The procedure returns the monthly appointment counts.

**Priority:** Medium
**Story Points:** 5
**Notes:**
- The statistics should be based on appointment data stored in MySQL.

## Patient User Stories

### 1. View Doctors

**Title:**
_As a patient, I want to view a list of doctors without logging in, so that I can explore my options before registering._

**Acceptance Criteria:**
1. The patient can access the doctor list without logging in.
2. The system displays available doctors.
3. Doctor information is displayed clearly.

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Doctor browsing should be publicly accessible.

### 2. Patient Sign Up

**Title:**
_As a patient, I want to sign up using my email and password, so that I can book appointments._

**Acceptance Criteria:**
1. The patient can provide an email and password.
2. The system validates the registration information.
3. A valid patient account is created successfully.

**Priority:** High
**Story Points:** 3
**Notes:**
- Email addresses should be unique.

### 3. Patient Login

**Title:**
_As a patient, I want to log into the portal, so that I can manage my bookings._

**Acceptance Criteria:**
1. The patient can enter their email and password.
2. Valid credentials allow access to the portal.
3. Invalid credentials are rejected.

**Priority:** High
**Story Points:** 3
**Notes:**
- Authentication should be secure.

### 4. Patient Logout

**Title:**
_As a patient, I want to log out of the portal, so that I can secure my account._

**Acceptance Criteria:**
1. The patient can log out from the portal.
2. The authenticated session is terminated.
3. Protected pages require login after logout.

**Priority:** High
**Story Points:** 2
**Notes:**
- Logout should securely terminate the session.

### 5. Book Appointment

**Title:**
_As a patient, I want to log in and book an hour-long appointment, so that I can consult with a doctor._

**Acceptance Criteria:**
1. The patient must be logged in to book an appointment.
2. The patient can select an available doctor and time slot.
3. The appointment duration is one hour.
4. A successful booking is saved in the system.

**Priority:** High
**Story Points:** 5
**Notes:**
- Only available time slots can be booked.

### 6. View Upcoming Appointments

**Title:**
_As a patient, I want to view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**
1. The patient can view their upcoming appointments.
2. Appointment details include the doctor and scheduled date and time.
3. Only the logged-in patient's appointments are displayed.

**Priority:** High
**Story Points:** 3
**Notes:**
- Past appointments should not appear in the upcoming appointments list.

## Doctor User Stories

### 1. Doctor Login

**Title:**
_As a doctor, I want to log into the portal, so that I can manage my appointments._

**Acceptance Criteria:**
1. The doctor can enter their username and password.
2. Valid credentials allow access to the doctor portal.
3. Invalid credentials are rejected.

**Priority:** High
**Story Points:** 3
**Notes:**
- Authentication should be secure.

### 2. Doctor Logout

**Title:**
_As a doctor, I want to log out of the portal, so that I can protect my data._

**Acceptance Criteria:**
1. The doctor can log out from the portal.
2. The authenticated session is terminated.
3. Protected pages require login after logout.

**Priority:** High
**Story Points:** 2
**Notes:**
- Logout should securely terminate the session.

### 3. View Appointment Calendar

**Title:**
_As a doctor, I want to view my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**
1. The doctor can access their appointment calendar.
2. Upcoming appointments are displayed with date and time.
3. Only the logged-in doctor's appointments are displayed.

**Priority:** High
**Story Points:** 3
**Notes:**
- The calendar should clearly show scheduled appointments.

### 4. Mark Unavailability

**Title:**
_As a doctor, I want to mark my unavailability, so that patients can see only the available appointment slots._

**Acceptance Criteria:**
1. The doctor can mark specific dates or time slots as unavailable.
2. Unavailable slots cannot be booked by patients.
3. Patients can only select available slots.

**Priority:** High
**Story Points:** 5
**Notes:**
- The system should prevent appointment conflicts.

### 5. Update Doctor Profile

**Title:**
_As a doctor, I want to update my profile with my specialization and contact information, so that patients have up-to-date information._

**Acceptance Criteria:**
1. The doctor can update their specialization.
2. The doctor can update their contact information.
3. Updated information is saved successfully and visible to patients.

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Profile information should be validated before saving.

### 6. View Patient Details

**Title:**
_As a doctor, I want to view patient details for upcoming appointments, so that I can be prepared._

**Acceptance Criteria:**
1. The doctor can access patient details for upcoming appointments.
2. The system displays the relevant patient information.
3. Only authorized doctors can access patient details.

**Priority:** High
**Story Points:** 5
**Notes:**
- Patient information must be protected from unauthorized access.
