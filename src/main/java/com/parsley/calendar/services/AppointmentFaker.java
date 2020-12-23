package com.parsley.calendar.services;

import com.github.javafaker.Faker;
import com.parsley.calendar.models.CreateAppointmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class AppointmentFaker {

    @Autowired
    @Value("#{new java.util.Random()}")
    private Random random;

    @Autowired
    @Value("#{new com.github.javafaker.Faker()}")
    private Faker faker;

    // No appointment can be scheduled further than 1 year in the future
    @Value("${appointment-faker.maxDaysToScheduleAhead:365}")
    private int maxDaysToScheduleAhead;

    // No appointment can be shorter than 15 minutes
    @Value("${appointment-faker.minDuration:15}")
    private int minDuration;

    // No appointment can be longer than 2 hours
    @Value("${appointment-faker.maxDuration:120}")
    private int maxDuration;

    // Schedule duration in 5 minute increments
    @Value("${appointment-faker.durationIncrementSize:5}")
    private int durationIncrementSize;


    // If more statuses come along, we should centralize this logic that appears in three places :
    //      1) The initial Flyway schema
    //      2) The javax.validation.constraints on CreateAppointmentRequest
    //      3) Here, hard-coded
    private final List<String> statuses = Arrays.asList("Available", "Booked");

    public CreateAppointmentRequest create() {
        final Instant future = faker.date().future(maxDaysToScheduleAhead, TimeUnit.DAYS).toInstant();
        final ZonedDateTime scheduledDate = ZonedDateTime.ofInstant(future, ZoneOffset.UTC);

        final Integer durationInMinutes = generateInteger(minDuration, maxDuration, durationIncrementSize);
        final String status = chooseOne(statuses);

        return new CreateAppointmentRequest(scheduledDate, durationInMinutes, status);
    }

    public Integer generateInteger(final int min, final int max, final int step) {
        final List<Integer> choices = IntStream.iterate(min, i -> i + step)
            .limit(max / step)
            .boxed()
            .collect(Collectors.toList());

        return chooseOne(choices);
    }

    public <T> T chooseOne(final List<T> choices) {
        return choices.get(random.nextInt(choices.size()));
    }
}