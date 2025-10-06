package edu.rmit.cosc1295.carehome;

import java.util.ArrayList;

public class Nurse extends Staff {

    // Inherits from Staff
    /**
     * Construct them with id, name and password
     * @param id ID of nurse
     * @param name Name of nurse
     * @param password password of nurse
     */

    public Nurse(String id, String name, String password) {
        super(id, name, password);
    }


    // Move the resident to a different bed.
    /**
     * Move the resident to another bed
     * @param residentName name of resident
     * @param bedID ID of bed
     */

    public void moveResident(String residentName, int bedId) {
        System.out.println("Nurse " + name + " requested to move "
                + residentName + " to bed " + bedId + ".");

        // Create log message
        CareHome.createLog("Nurse " + this.getName() + " moved resident "
                + residentName + " to bed " + bedId);
    }


    /**
     * This list stores all the shifts assigned to the nurse
     */
    private final ArrayList<Shift> nurseShifts = new ArrayList<>();


    /**
     * Add new shift to nurse
     * @param shift
     */

    public void addShift(Shift shift) {
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

