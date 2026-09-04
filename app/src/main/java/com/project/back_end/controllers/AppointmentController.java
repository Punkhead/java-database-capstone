package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Service service;

    public AppointmentController(
            AppointmentService appointmentService,
            Service service) {

        this.appointmentService = appointmentService;
        this.service = service;
    }

    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(
            @PathVariable String date,
            @PathVariable String patientName,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> tokenResponse =
                service.validateToken(token, "doctor");

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return tokenResponse;
        }

        LocalDate appointmentDate = LocalDate.parse(date);

        return ResponseEntity.ok(
                appointmentService.getAppointment(
                        patientName,
                        appointmentDate,
                        token)
        );
    }

    @PostMapping("/{token}")
    public ResponseEntity<?> bookAppointment(
            @RequestBody Appointment appointment,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> tokenResponse =
                service.validateToken(token, "patient");

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return tokenResponse;
        }

        int validationResult =
                service.validateAppointment(appointment);

        if (validationResult == 0) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Appointment time is not available.");

            return ResponseEntity.badRequest().body(response);
        }

        if (validationResult == -1) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid doctor ID.");

            return ResponseEntity.badRequest().body(response);
        }

        int result =
                appointmentService.bookAppointment(appointment);

        Map<String, String> response = new HashMap<>();

        if (result == 1) {
            response.put(
                    "message",
                    "Appointment booked successfully.");

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        }

        response.put(
                "message",
                "Appointment slot is already taken.");

        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{token}")
    public ResponseEntity<?> updateAppointment(
            @RequestBody Appointment appointment,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> tokenResponse =
                service.validateToken(token, "patient");

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return tokenResponse;
        }

        return appointmentService.updateAppointment(appointment);
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable long id,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> tokenResponse =
                service.validateToken(token, "patient");

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return tokenResponse;
        }

        return appointmentService.cancelAppointment(id, token);
    }
}