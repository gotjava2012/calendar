package com.parsley.calendar.controllers;

import com.parsley.calendar.models.AppointmentResponse;
import com.parsley.calendar.services.AppointmentFaker;
import com.parsley.calendar.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/randomizer")
public class RandomizerController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentFaker appointmentFaker;

    @PostMapping
    public AppointmentResponse create() {
        // While we would never expose such an endpoint in production, mocking JSON for testing out a database is such a drag.
        // This exposes a single endpoint where, upon issuing an empty POST, it will create and store a randomly generated appointment.
        // curl -X POST http://localhost:8080/randomizer
        return appointmentService.create(appointmentFaker.create());
    }

}
