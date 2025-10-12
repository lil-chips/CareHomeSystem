package edu.rmit.cosc1295.carehome;

import java.util.ArrayList;
import java.util.HashMap;

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
        // Calculate total hours already assigned on that day
        int totalHours = 0;
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

    /**
     * Check if this nurse is compliant with working hour rules.
     * @return true if all daily hours ≤ 8, false if any day exceeds limit
     */
    public boolean isCompliant() {
        HashMap<String, Integer> shiftMap = new HashMap<>();

        for (Shift shift : nurseShifts) {
            String whichDay = shift.getDay();
            int workingTime = shift.getDuration();  // shift hours
            int totalHours = shiftMap.getOrDefault(whichDay, 0) + workingTime;
        }
    }
}

