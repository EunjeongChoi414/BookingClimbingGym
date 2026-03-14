package com.project.domain.gym;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class BusinessHours {
    private final DayOfWeek day;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public BusinessHours(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DayOfWeek getDay() { return day; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
}