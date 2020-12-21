package com.parsley.calendar.controllers;

import com.parsley.calendar.CalendarApplication;
import com.parsley.calendar.models.AppointmentResponse;
import com.parsley.calendar.models.CreateAppointmentRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.parsley.calendar.services.AppointmentFaker;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;


@ActiveProfiles("test")
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CalendarApplication.class)
    @ExtendWith(SpringExtension.class)
    @Sql(scripts = "classpath:truncate_appointments.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    class AppointmentControllerTests {

        @LocalServerPort
        private int port;

        @Autowired
        private TestRestTemplate restTemplate;

        @Autowired
        private AppointmentFaker appointmentFaker;

        @Test
        void canCreateNewAppointment() {
            final ZonedDateTime now = Instant.now().atZone(ZoneOffset.UTC);
            final CreateAppointmentRequest request = appointmentFaker.create();

            // Create a new appointment
            final ResponseEntity<AppointmentResponse> httpResponse = restTemplate.postForEntity(getUrl(), request, AppointmentResponse.class);
            Assertions.assertTrue(httpResponse.getStatusCode().is2xxSuccessful(), String.valueOf(httpResponse.getStatusCodeValue()));

            final AppointmentResponse response = httpResponse.getBody();
            Assertions.assertNotNull(response);
            Assertions.assertNotNull(response.id);
            Assertions.assertTrue(now.isBefore(response.createdAt));
            Assertions.assertTrue(request.scheduledDate.isEqual(response.scheduledDate));
            Assertions.assertEquals(request.durationInMinutes, response.durationInMinutes);
            Assertions.assertEquals(request.status, response.status);
        }

        @Test
        void canListNewAppointments() {
            final int numberOfAppointmentsToCreate = 10;

            // Create 10 new random appointments
            Stream.generate(appointmentFaker::create)
                    .limit(numberOfAppointmentsToCreate)
                    .forEach(r -> {
                        final ResponseEntity<AppointmentResponse> createHttpResponse = restTemplate.postForEntity(getUrl(), r, AppointmentResponse.class);
                        Assertions.assertTrue(createHttpResponse.getStatusCode().is2xxSuccessful(), String.valueOf(createHttpResponse.getStatusCodeValue()));
                    });

            // Ensure we can list all 10 appointments
            final ResponseEntity<AppointmentResponse[]> listHttpResponse = restTemplate.getForEntity(getUrl(), AppointmentResponse[].class);
            Assertions.assertTrue(listHttpResponse.getStatusCode().is2xxSuccessful(), String.valueOf(listHttpResponse.getStatusCodeValue()));

            final AppointmentResponse[] response = listHttpResponse.getBody();
            Assertions.assertNotNull(response);
            Assertions.assertEquals(numberOfAppointmentsToCreate, response.length);
        }

        @Test
        void canFindNewAppointment() {
            final CreateAppointmentRequest request = appointmentFaker.create();

            // Create a new appointment
            final ResponseEntity<AppointmentResponse> createHttpResponse = restTemplate.postForEntity(getUrl(), request, AppointmentResponse.class);
            Assertions.assertTrue(createHttpResponse.getStatusCode().is2xxSuccessful(), String.valueOf(createHttpResponse.getStatusCodeValue()));

            final AppointmentResponse createResponse = createHttpResponse.getBody();
            Assertions.assertNotNull(createResponse);
            Assertions.assertNotNull(createResponse.id);

            // Find the newly created appointment by its ID
            final String findAppointmentUrl = String.format("%s/%s", getUrl(), createResponse.id);
            final ResponseEntity<AppointmentResponse> findHttpResponse = restTemplate.getForEntity(findAppointmentUrl, AppointmentResponse.class);
            Assertions.assertTrue(findHttpResponse.getStatusCode().is2xxSuccessful(), String.valueOf(findHttpResponse.getStatusCodeValue()));

            final AppointmentResponse findResponse = findHttpResponse.getBody();
            Assertions.assertNotNull(findResponse);
            Assertions.assertEquals(createResponse, findResponse);
        }

        @Test
        void canFindNewAppointmentWithoutLosingPricePrecision() {
            final ZonedDateTime scheduledDate = Instant.now().atZone(ZoneOffset.UTC).plus(1, ChronoUnit.DAYS);
            final Integer durationInMinutes = 15;
            final String status = "Available";

            final CreateAppointmentRequest request = new CreateAppointmentRequest(
                    scheduledDate,
                    durationInMinutes,
                    status

            );

            // Create a new appointment
            final ResponseEntity<AppointmentResponse> createHttpResponse = restTemplate.postForEntity(getUrl(), request, AppointmentResponse.class);
            Assertions.assertTrue(createHttpResponse.getStatusCode().is2xxSuccessful(), String.valueOf(createHttpResponse.getStatusCodeValue()));

            final AppointmentResponse createResponse = createHttpResponse.getBody();
            Assertions.assertNotNull(createResponse);
            Assertions.assertNotNull(createResponse.id);

            // Find the newly created appointment by its ID
            final String findAppointmentUrl = String.format("%s/%s", getUrl(), createResponse.id);
            final ResponseEntity<AppointmentResponse> findHttpResponse = restTemplate.getForEntity(findAppointmentUrl, AppointmentResponse.class);
            Assertions.assertTrue(findHttpResponse.getStatusCode().is2xxSuccessful(), String.valueOf(findHttpResponse.getStatusCodeValue()));

            final AppointmentResponse findResponse = findHttpResponse.getBody();
            Assertions.assertNotNull(findResponse);
            Assertions.assertEquals(createResponse, findResponse);
        }

        @Test
        void canDeleteNewAppointment() {
            final CreateAppointmentRequest request = appointmentFaker.create();

            // Create a new appointment
            final ResponseEntity<AppointmentResponse> createHttpResponse = restTemplate.postForEntity(getUrl(), request, AppointmentResponse.class);
            Assertions.assertTrue(createHttpResponse.getStatusCode().is2xxSuccessful(), String.valueOf(createHttpResponse.getStatusCodeValue()));

            final AppointmentResponse createResponse = createHttpResponse.getBody();
            Assertions.assertNotNull(createResponse);
            Assertions.assertNotNull(createResponse.id);

            // Delete created appointment by its ID
            restTemplate.delete(String.format("%s/%s", getUrl(), createResponse.id));

            // Ensure we no longer can find the deleted appointment by its ID
            final String findAppointmentUrl = String.format("%s/%s", getUrl(), createResponse.id);
            final ResponseEntity<AppointmentResponse> findHttpResponse = restTemplate.getForEntity(findAppointmentUrl, AppointmentResponse.class);
            Assertions.assertTrue(findHttpResponse.getStatusCode().isError());
        }

        @Test
        void canSearchAppointmentBetweenDates() {
            final CreateAppointmentRequest request = appointmentFaker.create();

            // Create a new appointment
            final ResponseEntity<AppointmentResponse> createHttpResponse = restTemplate.postForEntity(getUrl(), request, AppointmentResponse.class);
            Assertions.assertTrue(createHttpResponse.getStatusCode().is2xxSuccessful(), String.valueOf(createHttpResponse.getStatusCodeValue()));

            final AppointmentResponse createResponse = createHttpResponse.getBody();
            Assertions.assertNotNull(createResponse);
            Assertions.assertNotNull(createResponse.id);

            final ZonedDateTime start = request.scheduledDate.minus(1, ChronoUnit.HOURS);
            final ZonedDateTime end = request.scheduledDate.plus(1, ChronoUnit.HOURS);

            // Search for the newly created appointment by a start and end datetime - 1 hour in both directions
            final String findAppointmentUrl = String.format("%s?start={start}&end={end}", getUrl());
            final ResponseEntity<AppointmentResponse[]> searchHttpResponse = restTemplate.getForEntity(
                    findAppointmentUrl,
                    AppointmentResponse[].class,
                    start,
                    end
            );

            Assertions.assertTrue(searchHttpResponse.getStatusCode().is2xxSuccessful(), String.valueOf(searchHttpResponse.getStatusCodeValue()));

            final AppointmentResponse[] searchResponse = searchHttpResponse.getBody();
            Assertions.assertNotNull(searchResponse);
            Assertions.assertEquals(1, searchResponse.length);
            Assertions.assertEquals(createResponse, searchResponse[0]);
        }

        // TODO : Implement negative tests to exercise @Validate
        // TODO : Implement tests demoing lack of DB constraints

        private String getUrl() {
            return String.format("http://localhost:%s/appointments", this.port);
        }
}