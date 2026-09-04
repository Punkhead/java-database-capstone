package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Service(
            TokenService tokenService,
            AdminRepository adminRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            DoctorService doctorService,
            PatientService patientService) {

        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public ResponseEntity<Map<String, String>> validateToken(
            String token,
            String user) {

        Map<String, String> response = new HashMap<>();

        try {
            if (token == null || token.isBlank()) {
                response.put("message", "Token is missing.");
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            boolean valid = tokenService.validateToken(token, user);

            if (!valid) {
                response.put("message", "Invalid or expired token.");
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Token validation failed.");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }
    }

    public ResponseEntity<Map<String, String>> validateAdmin(
            Admin receivedAdmin) {

        Map<String, String> response = new HashMap<>();

        try {
            Admin admin =
                    adminRepository.findByUsername(
                            receivedAdmin.getUsername());

            if (admin == null ||
                    !admin.getPassword()
                            .equals(receivedAdmin.getPassword())) {

                response.put("message", "Invalid username or password.");

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            String token = tokenService.generateToken(
                    admin.getUsername());

            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Login failed.");

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    public Map<String, Object> filterDoctor(
            String name,
            String specialty,
            String time) {

        boolean hasName =
                name != null && !name.isBlank() &&
                        !"null".equalsIgnoreCase(name);

        boolean hasSpecialty =
                specialty != null && !specialty.isBlank() &&
                        !"null".equalsIgnoreCase(specialty);

        boolean hasTime =
                time != null && !time.isBlank() &&
                        !"null".equalsIgnoreCase(time);

        if (hasName && hasSpecialty && hasTime) {
            return doctorService
                    .filterDoctorsByNameSpecilityandTime(
                            name, specialty, time);
        }

        if (hasName && hasSpecialty) {
            return doctorService
                    .filterDoctorByNameAndSpecility(
                            name, specialty);
        }

        if (hasName && hasTime) {
            return doctorService
                    .filterDoctorByNameAndTime(
                            name, time);
        }

        if (hasSpecialty && hasTime) {
            return doctorService
                    .filterDoctorByTimeAndSpecility(
                            specialty, time);
        }

        if (hasName) {
            return doctorService
                    .findDoctorByName(name);
        }

        if (hasSpecialty) {
            return doctorService
                    .filterDoctorBySpecility(specialty);
        }

        if (hasTime) {
            return doctorService
                    .filterDoctorsByTime(time);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctorService.getDoctors());

        return response;
    }

    public int validateAppointment(Appointment appointment) {

        if (appointment == null ||
                appointment.getDoctor() == null ||
                appointment.getAppointmentTime() == null) {
            return 0;
        }

        Long doctorId = appointment.getDoctor().getId();

        if (doctorId == null ||
                !doctorRepository.existsById(doctorId)) {
            return -1;
        }

        LocalDate date =
                appointment.getAppointmentTime().toLocalDate();

        LocalTime requestedTime =
                appointment.getAppointmentTime().toLocalTime();

        List<String> availableTimes =
                doctorService.getDoctorAvailability(
                        doctorId, date);

        for (String time : availableTimes) {
            if (time == null) {
                continue;
            }

            try {
                LocalTime availableTime =
                        LocalTime.parse(time);

                if (availableTime.equals(requestedTime)) {
                    return 1;
                }
            } catch (Exception ignored) {
                // Handles values such as "10:00 AM"
                try {
                    LocalTime availableTime =
                            LocalTime.parse(
                                    time.toUpperCase()
                                            .replace("AM", "")
                                            .replace("PM", "")
                                            .trim());

                    if (availableTime.equals(requestedTime)) {
                        return 1;
                    }
                } catch (Exception ignoredAgain) {
                    // Invalid availability value
                }
            }
        }

        return 0;
    }

    public boolean validatePatient(Patient patient) {

        if (patient == null) {
            return false;
        }

        Patient existing =
                patientRepository.findByEmailOrPhone(
                        patient.getEmail(),
                        patient.getPhone());

        return existing == null;
    }

    public ResponseEntity<Map<String, String>> validatePatientLogin(
            Login login) {

        Map<String, String> response = new HashMap<>();

        try {
            Patient patient =
                    patientRepository.findByEmail(
                            login.getIdentifier());

            if (patient == null ||
                    !patient.getPassword()
                            .equals(login.getPassword())) {

                response.put(
                        "message",
                        "Invalid email or password.");

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            String token = tokenService.generateToken(
                    patient.getEmail());

            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Login failed.");

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> filterPatient(
            String condition,
            String name,
            String token) {

        try {
            String email =
                    tokenService.extractIdentifier(token);

            Patient patient =
                    patientRepository.findByEmail(email);

            if (patient == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Patient not found.");

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(response);
            }

            boolean hasCondition =
                    condition != null &&
                            !condition.isBlank() &&
                            !"null".equalsIgnoreCase(condition);

            boolean hasName =
                    name != null &&
                            !name.isBlank() &&
                            !"null".equalsIgnoreCase(name);

            if (hasCondition && hasName) {
                return patientService.filterByDoctorAndCondition(
                        condition,
                        name,
                        patient.getId());
            }

            if (hasCondition) {
                return patientService.filterByCondition(
                        condition,
                        patient.getId());
            }

            if (hasName) {
                return patientService.filterByDoctor(
                        name,
                        patient.getId());
            }

            return patientService.getPatientAppointment(
                    patient.getId(),
                    token);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put(
                    "message",
                    "Failed to filter patient appointments.");

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}