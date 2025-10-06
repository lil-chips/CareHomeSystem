package edu.rmit.cosc1295.carehome;

public class Manager extends Staff {

    // Inherits from Staff
    /**
     * Construct them with id, name and password
     * @param id ID of manager
     * @param name Name of manager
     * @param password Password of manager
     */

    public Manager(String id, String name, String password) {
        super(id, name, password);
    }


    // Modify staff's password
    /**
     * Set up new password for staff
     * @param staff The staff whose password will be updated
     * @param newPassword The new password
     */

    public void modifyStaffPassword(Staff staff, String newPassword) {
        staff.setPassword(newPassword);
        System.out.println("Manager " + name + " reset password for " + staff.getName());

        // Create log message
        String showlog = "Manager " + name + "has reset the password for staff" + staff.getId();

        CareHome.createLog(showlog);

    }
}
