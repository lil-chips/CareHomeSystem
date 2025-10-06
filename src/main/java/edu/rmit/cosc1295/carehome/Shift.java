package edu.rmit.cosc1295.carehome;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Shift implements Serializable {
    private final String day; // Monday~Sunday
    private final String time; // two shift: 8am~16pm or 2pm~10pm

    /**
     * Construct a shift with a specific day and time.
     * @param day The day of the shift
     * @param time The time range of the shift
     */

    public Shift (String day, String time) {
        this.day = day;
        this.time = time;
    }


    /**
     * Get the day of the shift
     * @return The day of the shift
     */

    public String getDay() {
        return day;
    }


    /**
     * Get the time range of the shift
     * @return The time of the shift
     */

    public String getTime() {
        return time;
    }


    /**
     * Get a string representation of the shift
     * @return "Friday 08:00-16:00"
     */

    @Override
    public String toString() {
        return day + " " + time;
    }


    /**
     * Here to calculate the shift duration in hours
     * @return duration in hours
     */

    public int getShiftDuration() {

        // If time is null and it doesn't contain "-", send error
        if (time == null || !time.contains("-")) {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }

        // If it is not only two "-", then send error
        String[] sep = time.split("-");
        if (sep.length != 2) {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }

        String beginTime = sep[0].trim();
        String endTime = sep[1].trim();

        // Define expected time format: 24-hour "HH:mm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime start, end;

        try {
            // Parse input strings into LocalTime objects
            // This also validates the format (e.g., "25:99" will fail)
            start = LocalTime.parse(beginTime, formatter);
            end = LocalTime.parse(endTime, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Time must be in HH:MM format, got: " + time);
        }

        int duration = end.getHour() - start.getHour();

        // Handle overnight shift (e.g., 21:00 → 05:00)
        if (duration < 0) {
            duration += 24;
        }

        // Prevent 0-hour or excessively long shifts
        if (duration <= 0 || duration > 12) {
            throw new IllegalArgumentException("Shift duration not allowed: " + duration + " hours");
        }

        return duration;
    }
}

