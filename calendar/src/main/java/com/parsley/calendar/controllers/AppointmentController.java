package com.parsley.calendar.controllers;

import com.parsley.calendar.models.AppointmentResponse;
import com.parsley.calendar.models.CreateAppointmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.parsley.calendar.services.AppointmentService;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public AppointmentResponse create(@Valid @RequestBody final CreateAppointmentRequest request) {
        return appointmentService.create(request);
    }

    @GetMapping("/{id}")
    public AppointmentResponse find(@PathVariable final Long id) {
        return appointmentService.find(id);
    }

    @PutMapping("/{id}/{status}")
    public AppointmentResponse update(@PathVariable final Long id, @PathVariable final String status) {
        return appointmentService.update(id, status);
    }

    @DeleteMapping("/{id}")
    public AppointmentResponse delete(@PathVariable final Long id) {
        return appointmentService.delete(id);
    }

    @GetMapping
    public List<AppointmentResponse> search(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final ZonedDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final ZonedDateTime end
    ) {
        if (Optional.ofNullable(start).isPresent() && Optional.ofNullable(end).isPresent()) {
            return appointmentService.search(start, end);
        } else {
            return appointmentService.list();
        }
    }
}
