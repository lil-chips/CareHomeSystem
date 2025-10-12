package edu.rmit.cosc1295.carehome;

import java.util.ArrayList;
import java.util.HashSet;

public class Doctor extends Staff {

    // Inherits from Staff
    /**
     * Construct them with id, name and password
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
     * Print all shifts
     */

    public void printShifts() {
        if (doctorShifts.isEmpty()) {
            System.out.println("Doctor " + name + " has no shifts assigned.");
        } else {
            System.out.println("Shifts for Doctor " + name + ":");
            for (Shift s : doctorShifts) {
                System.out.println(" - " + s);
            }
        }
    }

    /**
     * Create a new prescription
     * @param medicine the name of the medicine
     * @param dose the dosage or amount to take
     * @param time the time when the medicine should be taken
     * @return a new prescription created by this doctor
     */

    public Prescription createPrescription(String medicine, String dose, String time) {
        return new Prescription(this.id, medicine, dose, time);
    }

    /**
     * Add prescription for resident
     * @param careHome the care home instance
     * @param r resident
     * @param p prescription
     */

    public void addPrescription(CareHome careHome, Resident r, Prescription p) {
        if (careHome == null) {
            throw new IllegalArgumentException("CareHome reference can't be null.");
        }
        if (r == null) {
            throw new IllegalArgumentException("Resident can't be null.");
        }
        if (p == null) {
            throw new IllegalArgumentException("Prescription can't be null.");
        }

        // Add prescription in list
        r.addPrescription(p);

        System.out.println("Doctor " + this.getName() + " has written a new prescription for " + r.getName() + "."
                + "\n" + p);

        // Create log message
        CareHome.createLog("Doctor " + this.getName() + " added prescription for " + r.getName()
                + ": " + p);
    }

    /**
     * Check if this doctor is compliant with working hour rules.
     * @return true if 1 shift per day, false if violates rule
     */

    public boolean isCompliant() {
        HashSet<String> workDay = new HashSet<>();

        for (Shift s : doctorShifts) {
            if (workDay.contains(s.getDay())) {
                return false; // duplicate shift on the same day
            }
            if (s.getShiftDuration() != 1) {
                return false; // doctor shift must be only 1 hour
            }
            workDay.add(s.getDay());
        }
        return true;
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
