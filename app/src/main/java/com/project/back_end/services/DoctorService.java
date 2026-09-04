package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService) {

        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public List<String> getDoctorAvailability(
            Long doctorId,
            LocalDate date) {

        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

        if (doctor == null || doctor.getAvailableTimes() == null) {
            return List.of();
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay().minusNanos(1);

        List<Appointment> appointments =
                appointmentRepository
                        .findByDoctorIdAndAppointmentTimeBetween(
                                doctorId, start, end);

        List<String> bookedTimes = appointments.stream()
                .filter(a -> a.getAppointmentTime() != null)
                .map(a -> a.getAppointmentTime().toLocalTime().toString())
                .toList();

        return doctor.getAvailableTimes()
                .stream()
                .filter(time -> !bookedTimes.contains(time))
                .collect(Collectors.toList());
    }

    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            Doctor existingDoctor =
                    doctorRepository.findByEmail(doctor.getEmail());

            if (existingDoctor != null) {
                return -1;
            }

            doctorRepository.save(doctor);
            return 1;

        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public int updateDoctor(Doctor doctor) {
        try {
            if (doctor.getId() == null ||
                    !doctorRepository.existsById(doctor.getId())) {
                return -1;
            }

            doctorRepository.save(doctor);
            return 1;

        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    @Transactional
    public int deleteDoctor(long id) {
        try {
            if (!doctorRepository.existsById(id)) {
                return -1;
            }

            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);

            return 1;

        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {

        Map<String, String> response = new HashMap<>();

        try {
            Doctor doctor =
                    doctorRepository.findByEmail(login.getIdentifier());

            if (doctor == null ||
                    !doctor.getPassword().equals(login.getPassword())) {

                response.put("message", "Invalid email or password.");
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            String token = tokenService.generateToken(
                    doctor.getEmail()
            );

            response.put("token", token);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Login failed.");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @Transactional
    public Map<String, Object> findDoctorByName(String name) {

        Map<String, Object> response = new HashMap<>();

        List<Doctor> doctors =
                doctorRepository.findByNameLike(name);

        response.put("doctors", doctors);

        return response;
    }

    @Transactional
    public Map<String, Object> filterDoctorsByNameSpecilityandTime(
            String name,
            String specialty,
            String amOrPm) {

        Map<String, Object> response = new HashMap<>();

        List<Doctor> doctors =
                doctorRepository
                        .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                                name, specialty);

        response.put("doctors", filterDoctorByTime(doctors, amOrPm));

        return response;
    }

    @Transactional
    public Map<String, Object> filterDoctorByNameAndTime(
            String name,
            String amOrPm) {

        Map<String, Object> response = new HashMap<>();

        List<Doctor> doctors =
                doctorRepository.findByNameLike(name);

        response.put("doctors", filterDoctorByTime(doctors, amOrPm));

        return response;
    }

    @Transactional
    public Map<String, Object> filterDoctorByNameAndSpecility(
            String name,
            String specilty) {

        Map<String, Object> response = new HashMap<>();

        List<Doctor> doctors =
                doctorRepository
                        .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                                name, specilty);

        response.put("doctors", doctors);

        return response;
    }

    @Transactional
    public Map<String, Object> filterDoctorByTimeAndSpecility(
            String specilty,
            String amOrPm) {

        Map<String, Object> response = new HashMap<>();

        List<Doctor> doctors =
                doctorRepository.findBySpecialtyIgnoreCase(specilty);

        response.put("doctors", filterDoctorByTime(doctors, amOrPm));

        return response;
    }

    @Transactional
    public Map<String, Object> filterDoctorBySpecility(
            String specilty) {

        Map<String, Object> response = new HashMap<>();

        List<Doctor> doctors =
                doctorRepository.findBySpecialtyIgnoreCase(specilty);

        response.put("doctors", doctors);

        return response;
    }

    @Transactional
    public Map<String, Object> filterDoctorsByTime(
            String amOrPm) {

        Map<String, Object> response = new HashMap<>();

        List<Doctor> doctors = doctorRepository.findAll();

        response.put("doctors", filterDoctorByTime(doctors, amOrPm));

        return response;
    }

    private List<Doctor> filterDoctorByTime(
            List<Doctor> doctors,
            String amOrPm) {

        if (amOrPm == null || amOrPm.isBlank()) {
            return doctors;
        }

        String period = amOrPm.toUpperCase();

        return doctors.stream()
                .filter(doctor ->
                        doctor.getAvailableTimes() != null &&
                                doctor.getAvailableTimes()
                                        .stream()
                                        .anyMatch(time ->
                                                isTimeInPeriod(time, period)))
                .collect(Collectors.toList());
    }

    private boolean isTimeInPeriod(
            String time,
            String period) {

        if (time == null || time.isBlank()) {
            return false;
        }

        String normalized = time.trim().toUpperCase();

        if (normalized.endsWith("AM")) {
            return "AM".equals(period);
        }

        if (normalized.endsWith("PM")) {
            return "PM".equals(period);
        }

        try {
            int hour = Integer.parseInt(
                    normalized.split(":")[0]);

            if ("AM".equals(period)) {
                return hour < 12;
            }

            if ("PM".equals(period)) {
                return hour >= 12;
            }

        } catch (NumberFormatException ignored) {
        }

        return false;
    }
}