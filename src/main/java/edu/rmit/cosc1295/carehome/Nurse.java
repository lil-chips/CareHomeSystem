package edu.rmit.cosc1295.carehome;

import java.util.ArrayList;

public class Nurse extends Staff {

    /**
     * Construct them with id, name and password
     * Inherits from Staff
     * @param id ID of nurse
     * @param name Name of nurse
     * @param password password of nurse
     */

    public Nurse(String id, String name, String password) {
        super(id, name, password);
    }

    /**
     * Ask CareHome to move a resident to another bed
     * Only nurses can do this
     * @param careHome the CareHome that manages data
     * @param residentName name of resident
     * @param newBedId ID of bed
     */

    public void moveResident(CareHome careHome, String residentName, int newBedId) {

        // Ask the CareHome system to move the specified resident to a new bed.
        // 'this' = the nurse performing the move action
        careHome.moveResident(this, residentName, newBedId);

        // Print a message in console
        System.out.println("Nurse " + name + " requested to move "
                + residentName + " to bed " + newBedId + ".");

        // Create log message
        CareHome.createLog("Nurse " + this.getName() + " moved resident "
                + residentName + " to bed " + newBedId);
    }


    /**
     * This list stores all the shifts assigned to the nurse
     */
    private final ArrayList<Shift> nurseShifts = new ArrayList<>();


    /**
     * Add new shift to nurse
     * @param shift the shift object representing the new work shift
     */

    public void addShift(Shift shift) {
        // Calculate total hours already assigned on that day
        int totalHours = 0;

        // Run through all assigned shifts and sum up hours for the same day
        for (Shift s : nurseShifts) {
            if (s.getDay().equals(shift.getDay())) {
                totalHours += s.getShiftDuration();
            }
        }

        // Add the new shift hours
        totalHours += shift.getShiftDuration();

        // If more than 8 hours, throw exception
        if (totalHours > 8) {
            throw new NotWorkingException("Nurse " + getName() + " can't work more than 8 hours per day");
        }

        // Store shifts into list
        nurseShifts.add(shift);

        // Create log message
        CareHome.createLog("Shift assigned to Nurse " + this.getName() + ": " + shift);
    }


/**
     * Get all shifts assigned to the nurse
     * @return A list of shifts (day + time)
     */

    public ArrayList<Shift> getShifts() {
        return nurseShifts;
    }
}

