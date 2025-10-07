package edu.rmit.cosc1295.carehome;

import java.util.ArrayList;

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
     * @param shift
     */

    public void addShift(Shift shift) {
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

    public void printShifts() {
        if (doctorShifts.isEmpty()) {
            System.out.println("Doctor " + name + " has no shifts assigned.");
        } else {
            System.out.println("Shifts for Doctor " + name + ":");
        }
    }

    /**
     * Add prescription for resident
     * @param careHome the carehome instance
     * @param r resident
     * @param p prescription
     */

    public void addPrescription(CareHome careHome, Resident r, Prescription p) {
        r.addPrescription(p);

        System.out.println("Doctor " + this.getName() + " has written a new prescription for " + r.getName() + "."
                + "\n" + p);

        // Create log message
        CareHome.createLog("Doctor " + this.getName() + " added prescription for " + r.getName()
                + ": " + p);
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
