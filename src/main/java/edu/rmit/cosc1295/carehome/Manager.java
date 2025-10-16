package edu.rmit.cosc1295.carehome;

/**
 * Manager class is the system administrator
 * The manager can create new staff accounts and reset passwords.
 */

public class Manager extends Staff {

    /**
     * Construct them with id, name and password
     * Inherits from Staff
     * @param id ID of manager
     * @param name Name of manager
     * @param password Password of manager
     */

    public Manager(String id, String name, String password) {
        super(id, name, password);
    }

    /**
     * Allows a manager to update the password of a staff member.
     * @param staff The staff whose password will be updated
     * @param newPassword The new password
     */

    public void modifyStaffPassword(Staff staff, String newPassword) {
        // Staff can't be null
        if (staff == null) {
            throw new IllegalArgumentException("Staff can't be null");
        }
        // Password can't be null or blank
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password can't be null or blank");
        }

        try {
            // Update password
            staff.setPassword(newPassword);
            System.out.println("Manager " + name + " reset password for " + staff.getName());

            // Create log message
            String showlog = "Manager " + name + "has reset the password for " + staff.getClass().getSimpleName()
                    + " (ID: " + staff.getId() + ")";
            CareHome.createLog(showlog);
        } catch (Exception e) {
            // Log the failure and rethrow the exception
            CareHome.createLog("Password reset failed: " + staff.getName() + " due to: "
                    + e.getMessage());
            throw e;
        }
    }
}
