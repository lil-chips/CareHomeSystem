package edu.rmit.cosc1295.carehome;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

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
        // Normalize the time
        if (time.equalsIgnoreCase("Morning")) {
            this.time = "08:00-16:00";
        } else if (time.equalsIgnoreCase("Afternoon")) {
            this.time = "14:00-22:00";
        } else {
            this.time = time;
        }
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
            throw new IllegalArgumentException("Time must be in HH:MM format (e.g., 05:00-12:00), got: " + time);
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

    /**
     * Calculate the time duration with fixed pattern
     * if nothing here fits then call getShiftDuration() to calculate it
     * @return shift duration in hours
     */

    public int getDuration() {
        int hours = 0;

        // Check the shift patterns
        if (time.contains("8:00-16:00") || time.contains("08:00-16:00")) {
            hours = 8;
        }
        else if (time.contains("14:00-22:00")) {
            hours = 8;
        }
        else if (time.contains("1hr") || time.contains("1h")) {
            hours = 1;
        }
        else {
            // If the time doesn't match any pattern, then calculate it
            hours = getShiftDuration();
        }

        return hours;
    }

    // Multiple shift for staff is allowed
    protected ArrayList<Shift> shifts = new ArrayList<>();

    public ArrayList<Shift> getShifts() {
        return shifts;
    }
}

