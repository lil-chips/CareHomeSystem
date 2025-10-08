package edu.rmit.cosc1295.carehome;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class for all staff members (Manager, Doctor, Nurse).
 * Demonstrates encapsulation and inheritance.
 */
public class Staff implements Serializable{
    // Use protected so that subclasses can directly access the parent class
    protected String id; // Staff ID
    protected String name; // Staff name
    protected String password; // Login password

    /**
     * Construct them with id, name and password
     *
     * @param id the id of staff
     * @param name the name of staff
     * @param password the password of staff
     */

    public Staff(String id, String name, String password) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Staff ID cannot be null or blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Staff name cannot be null or blank.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank.");
        }

        this.id = id;
        this.name = name;
        this.password = password;
    }

    /**
     * Get the ID of staff
     * @return the ID of staff
     */

    public String getId() {
        return id;
    }

    /**
     * Get the name of staff
     * @return the name of staff
     */

    public String getName() {
        return name;
    }

    /**
     * Get the password of staff
     * @return the password of staff
     */

    public String getPassword() {
        return password;
    }

    /**
     * Get the new set-up password of the staff
     * Only manager can perform this action
     *
     * @param newPassword the new password of staff
     */

    public void setPassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.password = newPassword;
    }

    // Multiple shifts are allowed
    protected ArrayList<Shift> shifts = new ArrayList<>();

    /**
     * Get all shifts assigned to this staff
     * @return staff shifts
     */
    public ArrayList<Shift> getShifts() {
        return shifts;
    }

    /**
     * Show a short summary of the staff
     * @return formated string including id, name and role
     */

    @Override
    public String toString() {
        return "Staff ID: " + id + ", Name: " + name + " -> Role: " + this.getClass().getSimpleName();
    }

}

