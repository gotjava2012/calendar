package com.parsley.calendar.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;


@Data
@AllArgsConstructor
public class AppointmentResponse {

    public final Long id;
    public final ZonedDateTime createdAt;
    public final ZonedDateTime scheduledDate;
    public final Integer durationInMinutes;
    public final String status;



    @Override
    public String toString() {
        return "AppointmentResponse{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", scheduledDate=" + scheduledDate +
                ", durationInMinutes=" + durationInMinutes +
                ", status='" + status + '\'' +
                '}';
    }


}
