package edu.rmit.cosc1295.carehome;

/**
 * Manager class is the system administrator
 * The manager can create new staff accounts and reset passwords.
 */
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


    /**
     * Modify or set up staff's password
     *
     * @param staff The staff whose password will be updated
     * @param newPassword The new password
     */

    public void modifyStaffPassword(Staff staff, String newPassword) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password can't be null or blank");
        }

        try {
            staff.setPassword(newPassword);
            System.out.println("Manager " + name + " reset password for " + staff.getName());

            // Create log message
            String showlog = "Manager " + name + "has reset the password for " + staff.getClass().getSimpleName()
                    + " (ID: " + staff.getId() + ")";
            CareHome.createLog(showlog);
        } catch (Exception e) {
            CareHome.createLog("Password reset failed: " + staff.getName() + " due to: "
                    + e.getMessage());
            throw e;
        }
    }

    /**
     * Add a new staff member to the system.
     * Ask CareHome to handle saving and checking.
     *
     * @param careHome the CareHome that manages all data
     * @param newStaff the staff you want to add
     */

    public void addStaff(CareHome careHome, Staff newStaff) {
        careHome.addStaff(this, newStaff);
    }

    /**
     * Add a new resident and put them into a bed.
     * Ask CareHome to do the actual work.
     *
     * @param careHome the CareHome that manages all data
     * @param newResident the resident you want to add
     * @param bedId the bed number to assign
     */
    public void addResident(CareHome careHome, Resident newResident, int bedId) {
        careHome.addResident(this, newResident, bedId);
    }
}
