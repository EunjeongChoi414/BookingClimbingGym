package com.project.domain.gym;

import com.project.domain.exception.InvalidBusinessHoursException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Embeddable
public class BusinessHours {
    @Column(name = "day_of_week")
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;

    protected BusinessHours() {}

    public BusinessHours(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public static void validate(List<BusinessHours> businessHoursList) {
        for (BusinessHours bh : businessHoursList) {
            var start = bh.getStartTime();
            var end = bh.getEndTime();
            if (start == null && end == null) continue;
            if (start == null || end == null || end.isBefore(start)) {
                throw new InvalidBusinessHoursException();
            }
        }
    }
}