package com.parsley.calendar.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

public class CreateAppointmentRequest {

    @NotNull
    @Future
    @DateTimeFormat(iso = ISO.DATE_TIME)
    public final ZonedDateTime scheduledDate;

    @NotNull
    @Positive
    public final Integer durationInMinutes;

    @Pattern(regexp = "Available|Booked")
    public final String status;

    @JsonCreator
    public CreateAppointmentRequest(
        @JsonProperty final ZonedDateTime scheduledDate,
        @JsonProperty final Integer durationInMinutes,
        @JsonProperty final String status
    ) {
        this.scheduledDate = scheduledDate;
        this.durationInMinutes = durationInMinutes;
        this.status = status;
    }
}