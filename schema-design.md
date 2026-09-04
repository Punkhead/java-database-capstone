# Smart Clinic Management System - Schema Design

## MySQL Database Design

### Table: patients

- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(100), Not Null
- last_name: VARCHAR(100), Not Null
- email: VARCHAR(150), Not Null, Unique
- password: VARCHAR(255), Not Null
- phone: VARCHAR(20)
- date_of_birth: DATE
- created_at: DATETIME, Not Null

### Table: doctors

- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(100), Not Null
- last_name: VARCHAR(100), Not Null
- email: VARCHAR(150), Not Null, Unique
- password: VARCHAR(255), Not Null
- specialization: VARCHAR(150), Not Null
- phone: VARCHAR(20)
- available: BOOLEAN, Not Null
- created_at: DATETIME, Not Null

### Table: appointments

- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id), Not Null
- patient_id: INT, Foreign Key → patients(id), Not Null
- appointment_time: DATETIME, Not Null
- end_time: DATETIME, Not Null
- status: VARCHAR(20), Not Null
- notes: TEXT
- created_at: DATETIME, Not Null

### Table: admin

- id: INT, Primary Key, Auto Increment
- username: VARCHAR(100), Not Null, Unique
- email: VARCHAR(150), Not Null, Unique
- password: VARCHAR(255), Not Null
- created_at: DATETIME, Not Null

### Relationships and Constraints

- A patient can have many appointments.
- A doctor can have many appointments.
- Each appointment belongs to one patient and one doctor.
- Patient and doctor email addresses must be unique.
- Appointments must reference existing patients and doctors.
- A doctor should not have overlapping appointments.
- Appointment history should be retained even after an appointment is completed.
- Deleting a patient or doctor should not automatically delete appointment history. Instead, the account can be deactivated or the relationship can be handled according to application business rules.
- Email and phone format validation will be handled by the application layer.

## MongoDB Collection Design

### Collection: prescriptions

Prescriptions are stored in MongoDB because prescription information may contain flexible and nested data that can evolve over time.

```json
{
  "_id": "ObjectId('64abc123456')",
  "patientId": 101,
  "doctorId": 25,
  "appointmentId": 501,
  "issuedAt": "2026-09-04T10:30:00",
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "frequency": "Every 6 hours",
      "duration": "5 days"
    },
    {
      "name": "Vitamin D",
      "dosage": "1000 IU",
      "frequency": "Once daily",
      "duration": "30 days"
    }
  ],
  "doctorNotes": "Patient should rest and drink plenty of fluids.",
  "refillCount": 1,
  "pharmacy": {
    "name": "Smart Clinic Pharmacy",
    "location": "Istanbul"
  },
  "tags": [
    "follow-up",
    "medication"
  ],
  "metadata": {
    "source": "doctor-portal",
    "version": 1
  }
}
