package edu.rmit.cosc1295.carehome;

import java.util.ArrayList;

public class Doctor extends Staff {

    /**
     * Construct them with id, name and password
     * Inherits from Staff
     * @param id ID of doctor
     * @param name Name of doctor
     * @param password Password of doctor
     */

    public Doctor(String id, String name, String password) {
        super(id, name, password);
    }

    /**
     * This list stores all the shifts assigned to the doctor
     */

    private final ArrayList<Shift> doctorShifts = new ArrayList<>();

    /**
     * Add new shift to doctor
     * @param shift the Shift object that contains the day and time for this doctor
     */

    public void addShift(Shift shift) {

        // Check if already has a shift on the same day
        for (Shift s : doctorShifts) {
            if (s.getDay().equals(shift.getDay())) {
                throw new NotWorkingException("Doctor " + getName() + " can only work 1 hour per day.");
            }
        }

        // Check the duration, is it exactly 1 hour
        if (shift.getShiftDuration() != 1) {
            throw new NotWorkingException("Doctor shifts must be exactly 1 hour long");
        }

        // Store shift into list
        doctorShifts.add(shift);

        // Create log message
        CareHome.createLog("Shift assigned to Doctor " + this.getName() + ": " + shift);
    }


    /**
     * Get all shifts assigned to the doctor
     * @return A list of shifts (day + time)
     */

    public ArrayList<Shift> getShifts() {
        return doctorShifts;
    }

    /**
     * For debugging
     * @return doctor name+ID+shift
     */

    @Override
    public String toString() {
        return "Doctor " + name + " , ID: " + id + " -> Shifts: " + doctorShifts.size();
    }
}
