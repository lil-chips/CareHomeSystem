package edu.rmit.cosc1295.carehome;

import java.io.Serializable;

public class Bed implements Serializable {
    private final int bedId; // ID of the bed
    private boolean status; // Is the bed available?
    protected Resident resident; // The resident on the bed

    /**
     * Construct a bed with bed ID and status.
     * @param bedId ID of bed
     */

    public Bed(int bedId) {
        this.bedId = bedId;
        this.status = true;
    }

    /**
     * Get the ID of the bed
     * @return bed ID
     */

    public int getBedId() {
        return bedId;
    }

    /**
     * Check if the bed is available or not
     * @return is available or not
     */

    public boolean bedAvailable() {
        return status;
    }

    /**
     * Set occupancy status
     * @param status true = available, false = occupied
     */

    public void setAvailable(boolean status) {
        this.status = status;
    }

    /**
     * Get the resident object on this bed
     * @return resident object or null if empty
     */

    public Resident getResident() {
        return this.resident;
    }


    /**
     * Remove the resident on this bed
     */

    public void removeResident() {
        this.resident = null;
        this.status = true; // The bed is empty
    }

    /**
     * Assign a resident to a bed
     * If someone was already here, will be replaced
     * @param r resident to assign
     */

    public void assignResident(Resident r) {
        this.resident = r;
        this.status = false; // the bed is occupied
    }

    /**
     * Print the status of the bed
     */

    @Override
    public String toString() {
        if (this.resident == null) { // If the bed is empty
            return "Bed " + this.bedId + " is empty";
        } else { // If the bed has a resident, then print bed + resident name
            return "Bed " + this.bedId + " has resident: " + this.resident.getName();
        }
    }
}

