package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;
    private final Service service;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            TokenService tokenService,
            Service service) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
        this.service = service;
    }

    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(
            Appointment appointment) {

        Map<String, String> response = new HashMap<>();

        try {
            if (appointment.getId() == null ||
                    !appointmentRepository.existsById(appointment.getId())) {

                response.put("message", "Appointment not found.");
                return ResponseEntity.badRequest().body(response);
            }

            appointmentRepository.save(appointment);

            response.put("message", "Appointment updated successfully.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Failed to update appointment.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(
            long id,
            String token) {

        Map<String, String> response = new HashMap<>();

        try {
            var optionalAppointment = appointmentRepository.findById(id);

            if (optionalAppointment.isEmpty()) {
                response.put("message", "Appointment not found.");
                return ResponseEntity.badRequest().body(response);
            }

            Appointment appointment = optionalAppointment.get();

            appointmentRepository.delete(appointment);

            response.put("message", "Appointment cancelled successfully.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Failed to cancel appointment.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Transactional
    public Map<String, Object> getAppointment(
            String pname,
            LocalDate date,
            String token) {

        Map<String, Object> response = new HashMap<>();

        try {
            // TokenService üzerinden doktor ID'si alınacak.
            // Buradaki method adı TokenService.java'daki gerçek metoda
            // göre tamamlanmalı.

            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay().minusNanos(1);

            List<Appointment> appointments;

            // Doctor ID token'dan alındıktan sonra:
            // appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(...)

            appointments = List.of();

            response.put("appointments", appointments);
            return response;

        } catch (Exception e) {
            response.put("appointments", List.of());
            return response;
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> changeStatus(
            long id,
            int status) {

        Map<String, String> response = new HashMap<>();

        try {
            appointmentRepository.updateStatus(status, id);

            response.put("message", "Appointment status updated successfully.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Failed to update appointment status.");
            return ResponseEntity.badRequest().body(response);
        }
    }
}