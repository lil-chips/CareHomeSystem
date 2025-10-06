package edu.rmit.cosc1295.carehome;

import java.io.Serializable;

public class Staff implements Serializable{
    // Use protected so that subclasses can directly access the parent class
    protected String id; // Staff ID
    protected String name; // Staff name
    protected String password; // Login password

    /**
     * Construct them with id, name and password
     * @param id the id of staff
     * @param name the name of staff
     * @param password the password of staff
     */

    public Staff(String id, String name, String password) {
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
     * Get the new set up password of the staff
     * @param newPassword the new password of staff
     */

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

}

