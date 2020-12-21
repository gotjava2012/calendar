package com.parsley.calendar.services;

import com.parsley.calendar.entities.Appointment;
import com.parsley.calendar.entities.Status;
import com.parsley.calendar.models.AppointmentResponse;
import com.parsley.calendar.models.CreateAppointmentRequest;
import com.parsley.calendar.repositories.AppointmentRepository;
import com.parsley.calendar.repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private StatusRepository statusRepository;

    public AppointmentResponse create(final CreateAppointmentRequest request) {

        // Find the requested status - if it doesn't exist, we will not move forward.
        final Status status = findStatus(request.status);

        final Appointment appointment = new Appointment();

        // Persist to the database
        return from(appointmentRepository.save(appointment));
    }

    public List<AppointmentResponse> list() {
        return appointmentRepository
                .findAll()
                .stream()
                .map(AppointmentService::from)
                .collect(Collectors.toList());
    }

    public AppointmentResponse find(final Long id) {
        return from(
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown id %s", id)))
        );
    }

    public AppointmentResponse update(final Long id, final String status) {

        // Find the requested appointment by ID - if it doesn't exist, we cannot update it
        final Appointment foundAppointment = appointmentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown id %s", id)));

        foundAppointment.setStatus(findStatus(status));

        return from(appointmentRepository.save(foundAppointment));
    }

    public AppointmentResponse delete(final Long id) {

        // Find the requested appointment by ID - if it doesn't exist, we cannot update it
        final Appointment foundAppointment = appointmentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown id %s", id)));

        appointmentRepository.delete(foundAppointment);

        return from(foundAppointment);
    }

    public List<AppointmentResponse> search(final ZonedDateTime start, final ZonedDateTime end) {
        return appointmentRepository.findAll()
                .stream()
                .map(AppointmentService::from)
                .collect(Collectors.toList());
    }

    public Status findStatus(final String status) {
        return statusRepository
                .findOne(Example.of(new Status(status)))
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown status %s", status)));
    }

    private static AppointmentResponse from(final Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getCreatedAt(),
                appointment.getScheduledDate(),
                appointment.getDurationInMinutes(),
                appointment.getStatus().getStatus()

        );
    }

}
