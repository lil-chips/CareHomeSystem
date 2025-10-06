package edu.rmit.cosc1295.carehome;

import java.io.Serializable;
import java.util.*;

public class Resident implements Serializable {
    protected String name; // Name of resident
    protected String gender; // Gender of resident
    protected Integer bedId; // Current bed ID

    /**
     * Here to save the prescription of residents
     * A resident can have many prescriptions
     */

    protected ArrayList<Prescription> prescriptions = new ArrayList<>();


    // Not assign to bed yet
    /**
     * Construct with resident's name and gender, bed not assigned yet.
     * @param name Name of resident
     * @param gender Gender of resident
     */

    public Resident(String name, String gender) {
        this.name = name;
        this.gender = gender;
        this.bedId = null; // New resident might not hava a bed yet
    }


    // Assigned to bed already
    /**
     * Construct with resident's name, gender and bed ID
     * @param name Name of resident
     * @param gender Gender of resident
     * @param bedId ID of bed
     */

    public Resident(String name, String gender, Integer bedId) {
        this.name = name;
        this.gender = gender;
        this.bedId = bedId;
    }


    /**
     * Get the name of resident
     * @return the name of resident
     */

    public String getName() {
        return name;
    }


    /**
     * Get the gender of resident
     * @return the gender of resident
     */

    public String getGender() {
        return gender;
    }


    /**
     * Get the bed ID
     * @return bed ID
     */

    public Integer getBedId() {
        return bedId;
    }


    /**
     * Set or Change resident's bed id
     * @param bedId The new bed id to assign, or null if unassigned
     */

    public void saveBedId(Integer bedId) {
        if (bedId != null && bedId < 0) {
            throw new IllegalArgumentException("Bed ID cannot be negative: " + bedId);
        }
        this.bedId = bedId;
    }


    /**
     * Add a new prescription into the list
     * @param p
     */

    public void addNewPres(Prescription p) {
        this.prescriptions.add(p);
    }


    /**
     * Update prescription
     * @param i position
     * @param nP new prescription
     */

    public void updatePres(int i, Prescription nP) {
        if (i < 0 || i >= this.prescriptions.size()) {
            throw new IllegalArgumentException("The prescription doesn't exist: " + i);
        }

        if (nP == null) {
            throw new IllegalArgumentException("Prescription cannot be null");
        }

        this.prescriptions.set(i, nP);
    }


    /**
     * Return all the prescriptions
     * @return
     */

    public ArrayList<Prescription> getAllPres() {
        return this.prescriptions;
    }


    /**
     * Print all prescriptions
     */

    public void printAllPres() {
        if (this.prescriptions.isEmpty()) {
            System.out.println(this.name + " doesn't have any prescription");
        } else {
            for (int i = 0; i < this.prescriptions.size(); i++) {
                System.out.println((i + 1) + ". " + this.prescriptions.get(i));
            }
        }
    }
}

