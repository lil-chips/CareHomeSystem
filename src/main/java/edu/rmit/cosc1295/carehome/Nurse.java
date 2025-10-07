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


    /**
     * Ask CareHome to move a resident to another bed
     * Only nurses can do this
     *
     * @param careHome the CareHome that manages data
     * @param residentName name of resident
     * @param newBedId ID of bed
     */

    public void moveResident(CareHome careHome, String residentName, int newBedId) {
        careHome.moveResident(this, residentName, newBedId);

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

