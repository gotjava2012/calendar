package com.parsley.calendar.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.ZonedDateTime;
@ToString
@Data
@AllArgsConstructor
public class AppointmentResponse {

    public final Long id;
    public final ZonedDateTime createdAt;
    public final ZonedDateTime scheduledDate;
    public final Integer durationInMinutes;
    public final String status;

//    @JsonCreator
//    public AppointmentResponse(
//            @JsonProperty final Long id,
//            @JsonProperty final ZonedDateTime createdAt,
//            @JsonProperty final ZonedDateTime scheduledDate,
//            @JsonProperty final Integer durationInMinutes,
//            @JsonProperty final String status
//    ) {
//        this.id = id;
//        this.createdAt = createdAt;
//        this.scheduledDate = scheduledDate;
//        this.durationInMinutes = durationInMinutes;
//        this.status = status;
//    }

//    @Override
//    public String toString() {
//        return "AppointmentResponse{" +
//                "id=" + id +
//                ", createdAt=" + createdAt +
//                ", scheduledDate=" + scheduledDate +
//                ", durationInMinutes=" + durationInMinutes +
//                ", status='" + status + '\'' +
//                '}';
//    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AppointmentResponse)) {
            return false;
        }

        final AppointmentResponse that = (AppointmentResponse) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(createdAt, that.createdAt)
                .append(scheduledDate, that.scheduledDate)
                .append(durationInMinutes, that.durationInMinutes)
                .append(status, that.status)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(createdAt)
                .append(scheduledDate)
                .append(durationInMinutes)
                .append(status)
                .toHashCode();
    }
}
