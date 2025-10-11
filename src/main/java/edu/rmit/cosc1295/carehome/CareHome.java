package edu.rmit.cosc1295.carehome;

import javax.print.Doc;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.*;
import java.sql.*;



public class CareHome implements Serializable {

    // Store all the staffs
    private final ArrayList<Staff> staffList = new ArrayList<>();

    // Store all the residents
    private final ArrayList<Resident> residents = new ArrayList<>();

    // Store all the bed No.
    private final ArrayList<Bed> beds = new ArrayList<>();

    // Store all the logs (shared across all CareHome instances)
    private static final ArrayList<String> logged = new ArrayList<>();


    /**
     * Only manager can add new staff member
     * @param manager Who perform the action
     * @param newStaff The staff member to be added
     * @throws IllegalArgumentException if staff id already exists
     */

    public void addStaff(Manager manager, Staff newStaff) {
        // Only manager is allowed to add staff
        if (manager == null) {
            throw new UnauthorizedException("Only manager can add staff");
        }

        // Check for duplicate staff ID
        for (Staff s : staffList) {
            if (s.getId().equals(newStaff.getId())) {
                throw new IllegalArgumentException("Staff id already exists: " + newStaff.getId());
            }
        }

        // Add to in-memory list
        staffList.add(newStaff);
        System.out.println("Manager " + manager.getName() + " added a new staff: "
                + newStaff.getId() + " - " + newStaff.getName());

        // Create log message
        String showlog = "Manager " + manager.getName() + " add a new staff " + newStaff.getName() + "("
                + newStaff.getId() + ")";
        createLog(showlog);

        // Determine role from class type
        String role = newStaff.getClass().getSimpleName();

        // Save the new staff member into database
        CareHomeDatabase.insertStaff(newStaff.getId(), newStaff.getName(), role, newStaff.getPassword());
    }


    /**
     * Print all staff in the list
     */

    public void showStaff() {
        if (staffList.isEmpty()) {
            System.out.println("No staff");
        } else {
            for (Staff s : staffList) {
                System.out.println(s.getClass().getSimpleName() + " " + s.getId() + " - " + s.getName());
            }
        }
    }


    /**
     * Get all staff in the care home, for code facing
     * Give access to staff list
     * @return list of all staff
     */

    public ArrayList<Staff> getStaffList() {
        return staffList;
    }


    /**
     * Allows a Manager to add a new resident
     * @param manager Who perform the action
     * @param resident The resident to be added
     */

    public void addResident(Manager manager, Resident resident, int bedId) {

        // Only manager can perform this action
        if (manager == null) {
            throw new UnauthorizedException("Only manager can add resident");
        }

        // Find the target bed
        Bed targetBed = findBedById(bedId);

        // If the bed doesn't exist
        if (targetBed == null) {
            throw new IllegalArgumentException("Bed ID " + bedId + " does not exist");
        }

        // If the bed is occupied
        if (!targetBed.bedAvailable()) {
            throw new IllegalArgumentException("Bed ID " + bedId + " is not available");
        }

        // Assign the bed
        targetBed.assignResident(resident);
        resident.setBedId(bedId);
        residents.add(resident);

        // Save resident record to database
        CareHomeDatabase.insertResident(resident.getName(), resident.getGender(), resident.getBedId());

        // Create log message
        String showlog = "Manager " + manager.getName() + " (" + manager.getId() + ") added a new resident "
                + resident.getName() + " to bed " + bedId;
        System.out.println(showlog);
        createLog(showlog);
    }

    public void moveResident(Nurse nurse, String residentName, int newBedId) {
        if (nurse == null) {
            throw new UnauthorizedException("Only nurse can move resident");
        }
        if (residentName == null || residentName.isBlank()) {
            throw new IllegalArgumentException("Resident name can't be null or blank");
        }

        // Find the resident
        Resident target = null;
        for (Resident r : residents) {
            if (r.getName() != null && r.getName().equalsIgnoreCase(residentName)) {
                target = r;
                break;
            }
        }
        if (target == null) {
            throw new IllegalArgumentException("Resident " + residentName + " does not exist");
        }

        // Find a new bed
        Bed targetBed = null;
        for (Bed b : beds) {
            if (b.getBedId() == newBedId) {
                targetBed = b;
                break;
            }
        }
        if (targetBed == null) {
            throw new IllegalArgumentException("Bed ID " + newBedId + " does not exist");
        }
        if (!targetBed.bedAvailable()) {
            throw new IllegalArgumentException("Bed ID " + newBedId + " is not available");
        }

        // Release old bed
        Integer oldBedId = target.getBedId();
        if (oldBedId != null) {
            for (Bed b : beds) {
                if (b.getBedId() == oldBedId) {
                    b.removeResident(); // Set the status to available
                    break;
                }
            }
        }

        // Assign new bed + update resident's bedId
        targetBed.assignResident(target); // Set the status to occupied
        target.setBedId(newBedId);

        // Record
        createLog("Nurse " + nurse.getName() + " moved resident "
        + target.getName() + " to bed " + newBedId);
    }


    /**
     * Print out all the resident's name, gender and bed condition
     */

    public void printAllResidents() {
        if (residents.isEmpty()) {
            System.out.println("No residents");
        } else {
            for (Resident resident : residents) {
                String bedId;
                if (resident.getBedId() == null) {
                    bedId = "Not assigned to any bed yet";
                } else {
                    bedId = "BedNo. " + resident.getBedId();
                }
                System.out.println(resident.getName() + " (" + resident.getGender() + ") -> "
                        + bedId);
            }
        }
    }


    /**
     * Find a resident by name, return null if not found
     * @param name name of the resident to search for
     * @return The Resident object if found, otherwise null
     */

    public Resident findResidentByName(String name) {
        for (Resident re : residents) {
            if (re.getName().equalsIgnoreCase(name)) {
                return re;
            }
        }
        return null;
    }


    /**
     * Give access to resident list (for code facing)
     * @return list of all residents
     */

    public ArrayList<Resident> getResidents() {
        return residents;
    }


    /**
     * Add new bed to the list
     * @param bed The bed to be added
     */

    public void addBed(Bed bed) {
        for (Bed value : beds) {
            if (value.getBedId() == bed.getBedId()) {
                throw new IllegalArgumentException("Bed id already exists: " + bed.getBedId());
            }
        }
        beds.add(bed);
        System.out.println("Added BedNo. " + bed.getBedId());
    }


    /**
     * Print out all the beds and conditions
     */

    public void printAllBeds() {
        if (beds.isEmpty()) {
            System.out.println("No beds");
            return;
        }

        for (Bed b : beds) {
            System.out.println("BedNo. " + b.getBedId() + ", is it available? " + b.bedAvailable());
        }
    }


    /**
     * Give access to the bed list (for code facing)
     * @return list of all beds
     */

    public ArrayList<Bed> getBeds() {
        return beds;
    }


    /**
     * Assign a resident to a specific bed
     * @param s Who perform the action (doctor or nurse)
     * @param residentName The name of the resident
     * @param bedId The ID of the bed
     * @throws UnauthorizedException only doctor or nurse can perform this action
     * @throws NotWorkingException check doctor or nurse is on duty that day or not
     * @throws IllegalArgumentException if the bed or resident does not exist
     * @throws IllegalStateException if the bed is already occupied or resident already has a bed
     */

    public void assignResidentToBed(Staff s, String residentName, int bedId) {

        // Only nurse or doctor can assign a resident to a bed
        if (!(s instanceof Nurse || s instanceof Doctor)) {
            throw new UnauthorizedException("Only nurse or doctors can assign a resident to a bed");
        }

        // Check is the doc or nurse on duty today
        String today = java.time.LocalDate.now().getDayOfWeek().toString();
        if (!isWorking(s, today)) {
            throw new NotWorkingException(s.getName() + " is not working today (" + today + ")");
        }

        // Find the target bed
        Bed targetbed = findBedById(bedId);

        // If it can't find the bed ID, then throw error
        if (targetbed == null) {
            throw new IllegalArgumentException("Can't find bed ID" + bedId);
        }

        // Bed must be empty
        // If bedAvailable == false, which means is occupied
        if (!targetbed.bedAvailable()) {
            throw new IllegalStateException("The bed " + bedId + " is occupied!");
        }

        // Find the resident by name
        Resident re = findResidentByName(residentName);
        if (re == null) {
            throw new IllegalArgumentException("Can't find the resident named: " + residentName);
        }

        // Resident has a bed or not
        if (re.getBedId() != null) {
            throw new IllegalStateException("The resident already has a bed: " + re.getBedId());
        }

        // Assign and update
        targetbed.assignResident(re);
        targetbed.setAvailable(false);
        re.setBedId(bedId);

        // Create log message + print to console
        String showlog = "Manager " + s.getName() + " (" + s.getId() + ") assigned resident " + re.getName() +
                " to bed " + bedId;
        System.out.println(showlog);
        CareHome.createLog(showlog);

        // Save the updated resident into the database
        CareHomeDatabase.insertResident(re.getName(), re.getGender(), bedId);
    }


    /**
     * This loop can arrange shifts for nurses
     * Currently assigns 2 shifts per day (14 shifts per week).
     * Later have to change it to maximum 1 shift/day
     */

    public void allocateNursesShifts() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] nurseShiftTime = {"08:00-16:00", "14:00-22:00"};
        Random random = new Random();

        // Search the whole staff list
        for (Staff staff : staffList) {
            // If the staff is a nurse then run the loop
            if (staff instanceof Nurse nurse) {

                // Prevent duplicate shift
                if (!nurse.getShifts().isEmpty()) {
                    System.out.println("Nurse " + nurse.getName() + " already has shifts assigned");
                    continue; // skip this nurse
                }

                // Randomly create Shifts for every day and time slot
                for (String day : days) {
                    String randomShift = nurseShiftTime[random.nextInt(nurseShiftTime.length)];
                    Shift shift = new Shift(day, randomShift);
                    nurse.addShift(shift);
                }


                System.out.println("Assigned 7 random shifts to Nurse (1 shift per day): " + nurse.getName());

                // Create log message + print to console
                String showlog = "Manager assigned 7 shifts to Nurse "
                        + nurse.getName() + "(" + nurse.getId() + ")";
                System.out.println(showlog);
                CareHome.createLog(showlog);

            }
        }
    }


    /**
     * This loop can arrange shifts for doctors
     * One shift per day
     */

    public void allocateDoctorsShifts() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String doctorShiftTime = "09:00-10:00";

        for (Staff staff : staffList) {

            // Only apply to doctor objects
            if (staff instanceof Doctor doctor) {

                // Prevent duplicate shift
                if (!doctor.getShifts().isEmpty()) {
                    System.out.println("Doctor " + doctor.getName() + " already has shifts assigned");
                    continue; // skip this doctor
                }

                // Assign 1-hour shift per day
                for (String day : days) {
                    Shift shift = new Shift(day, doctorShiftTime);
                    doctor.addShift(shift);
                }

                System.out.println("Assigned 7 shifts (1 per day) to Doctor: " + doctor.getName());

                // Create log message + print to console
                String showlog = "Manager assigned 7 shifts to doctor "
                        + doctor.getName() + "(" + doctor.getId() + ")";
                System.out.println(showlog);
                CareHome.createLog(showlog);
            }
        }
    }


    /**
     * Print all nurse shifts
     */

    public void printNurseShifts() {
        System.out.println("\n Nurse Shifts ");

        for (int i = 0; i < getStaffList().size(); i++) {
            Staff staff = getStaffList().get(i);

            if (staff instanceof Nurse nurse) {

                System.out.println("Shifts for " + nurse.getName() + ":");

                ArrayList<Shift> shifts = nurse.getShifts();

                for (Shift shift : shifts) {
                    System.out.println("  " + shift);
                }

                System.out.println("Total shifts: " + shifts.size());
                System.out.println("\n");
            }
        }
    }


    /**
     * Print all doctor shift
     */

    public void printDoctorShifts() {
        System.out.println("\n Doctor Shifts ");

        for (Staff staff : staffList) {
            if (staff instanceof Doctor doctor) {

                System.out.println("Shifts for " + doctor.getName() + ":");

                ArrayList<Shift> shifts = doctor.getShifts();

                for (Shift shift : shifts) {
                    System.out.println(shift);
                }

                System.out.println("Total shifts: " + shifts.size());
                System.out.println();
            }
        }
    }


    /**
     * Checks if any Nurse is scheduled to work more than 8 hours per day
     * @throws IllegalStateException if a Nurse exceeds 8 hours in a single day
     */

    public void checkCompliance() {
        for (Staff staff : staffList) {
            if (staff instanceof Nurse nurse) {
                ArrayList<Shift> shifts = nurse.getShifts();

                HashMap<String, Integer> shiftMap = new HashMap<>();

                for (Shift shift : shifts) {
                    String whichDay = shift.getDay(); // Get the day
                    int workingTime = shift.getDuration();

                    int hourComb = shiftMap.getOrDefault(whichDay, 0) + workingTime;

                    if (hourComb > 8) {
                        throw new IllegalStateException("Nurse " + nurse.getName()
                                + " works more than 8 hours on " + whichDay);
                    }

                    shiftMap.put(whichDay, hourComb);
                }

            }
        }
        System.out.println("All nurses are compliant with the shift rules.");
    }


    /**
     * The manager modifies a nurse's shift
     * @param manager The Manager performing the modification
     * @param nurse The Nurse whose shift will be modified
     * @param i The index of the shift to be replaced
     * @param newShift The new Shift object to assign
     */

    public void modifyNurseShift(Manager manager, Nurse nurse, int i, Shift newShift) {
        ArrayList<Shift> shifts = nurse.getShifts();

        // Check the index is correct or not
        if (i < 0 || i >= shifts.size()) {
            throw new IllegalArgumentException("Shift number is not valid: " + i);
        }

        // Get the old shifts
        Shift preShift = shifts.get(i);

        // Modified the old one to new, use set to combine
        shifts.set(i, newShift);

        // Create log message + print to console
        String showlog = "Manager " + manager.getName() + " has modified the shift for nurse "
                + nurse.getName() + "(" + nurse.getId() + ")" + " , from " + preShift.toString() + " to "
                + newShift.toString();
        System.out.println(showlog);
        CareHome.createLog(showlog);

        // Check does it over 8 hours
        checkCompliance();
    }


    /**
     * Allows a Manager to modify an existing shift of a Doctor.
     * Replaces the Doctor's shift at the specified index with a new Shift
     * @param manager The Manager performing the modification
     * @param doctor The Doctor whose shift will be modified
     * @param i The index of the shift to be replaced
     * @param newShift The new Shift object to assign
     */

    public void modifyDoctorShift(Manager manager, Doctor doctor, int i, Shift newShift) {
        ArrayList<Shift> shifts = doctor.getShifts();

        // Check the index is correct or not
        if (i < 0 || i >= shifts.size()) {
            throw new IllegalArgumentException("Shift number is not valid: " + i);
        }

        // Get the old shifts
        Shift preShift = shifts.get(i);

        // Modified the old one to new, use set to combine
        shifts.set(i, newShift);

        // Create log message + print to console
        String showlog = "Manager " + manager.getName() + " has modified the shift for doctor "
                + doctor.getName() + "(" + doctor.getId() + ")" + " , from " + preShift.toString() + " to "
                + newShift.toString();
        System.out.println(showlog);
        CareHome.createLog(showlog);

        // Check does it over 8 hours
        checkCompliance();
    }


    /**
     * Save current CareHome object to file
     * @param filename path to save
     */

    public void saveToFile(String filename) {
        try {
            try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename))) {
                output.writeObject(this);
            }
            System.out.println("Saved to " + filename);

            // Record log
            CareHome.createLog("System data saved to " + filename);

        } catch (IOException ioe) {
            System.err.println("Failed to save: " + ioe.getMessage());
        }
    }

    /**
     * Load a CareHome object from file
     * @param filename path to read from
     * @return loaded CareHome object
     */

    public static CareHome loadFromFile(String filename) {

        // Check does the file exist or not
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File " + filename + " does not exist.");
            return null;
        }

        try {
            CareHome home;
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename))) {
                home = (CareHome) input.readObject();
            }
            System.out.println("Loaded from " + filename);

            // Record log
            CareHome.createLog("System data loaded from " + filename);

            return home;

        } catch (IOException ioe) {
            System.err.println("Failed to load due to IO error: " + ioe.getMessage());
            return null;
        } catch (ClassNotFoundException c) {
            System.err.println("Failed to load due to missing class: " + c.getMessage());
            return null;
        }
    }


    /**
     * Allows a Doctor to add a new prescription for a resident
     * @param doctor The Doctor writing the prescription
     * @param bedId    The ID of the bed where the resident is assigned
     * @param medicine The name of the medicine
     * @param dose The dosage of the medicine
     * @param time The time when the medicine should be taken
     */

    public void docAddPres(Doctor doctor, int bedId, String medicine,
                           String dose, String time) {

        if (doctor == null) {
            throw new UnauthorizedException("Only doctor can add prescriptions.");
        }

        // method in line 920
        Bed bed = findBedById(bedId);

        // If bed is null then throw error
        if (bed == null) {
            throw new IllegalArgumentException("Can't find bed: " + bedId);
        }


        // Search the resident
        Resident r = bed.getResident();
        if (r == null) {
            throw new IllegalArgumentException("No resident in bed: " + bedId);
        }

        // Check if the doctor is working today or not
        String today = java.time.LocalDate.now().getDayOfWeek().toString();
        if (!isWorking(doctor, today)) {
            throw new NotWorkingException("Doctor " + doctor.getName() + " is not working today (" + today + ")");
        }

        // Write a new prescription
        Prescription p = new Prescription(doctor.getId(), medicine, dose, time);

        // Add prescription into resident's prescription list
        r.getPrescriptions().add(p);

        // Save prescription into database
        int residentId = getResidentId(r);
        // If it's not null then add it into database
        if (residentId != -1) {
            CareHomeDatabase.insertPrescription(residentId, doctor.getId(), medicine, dose, time);
        }

        // Create log message + print to console
        String showlog = "Doctor " + doctor.getName() + " (" + doctor.getId() + ") added prescription " + medicine +
                " (" + dose + ")" + " at " + time + " to " + r.getName();
        // Print out the message
        System.out.println(showlog);
        CareHome.createLog(showlog);
    }


    /**
     * Allows a Doctor to update an existing prescription of a resident
     * @param doctor The Doctor updating the prescription
     * @param bedId    The ID of the bed where the resident is assigned
     * @param numberOrdered The index of the prescription to update
     * @param newMedicine The new medicine name
     * @param newDose The new dosage
     * @param newTime The new administration time
     */

    public void docUpdatePres(Doctor doctor, int bedId, int numberOrdered, String newMedicine,
                              String newDose, String newTime) {

        // If is not a doctor show error message
        if (doctor == null) {
            throw new UnauthorizedException("Only doctor can update prescriptions.");
        }

        // method in line 920
        Bed bed = findBedById(bedId);

        // If bed is null then throw error
        if (bed == null) {
            throw new IllegalArgumentException("Can't find bed: " + bedId);
        }

        // Search the resident
        Resident r = bed.getResident();
        if (r == null) {
            throw new IllegalArgumentException("No resident in bed: " + bedId);
        }

        // Check the prescription index
        if (numberOrdered < 0 || numberOrdered >= r.getPrescriptions().size()) {
            throw new IllegalArgumentException("Number of prescriptions doesn't exist: " + numberOrdered);
        }

        // Check is it the same doctor
        Prescription oldPrescription = r.getPrescriptions().get(numberOrdered);
        if (!oldPrescription.getDoctorId().equals(doctor.getId())) {
            throw new UnauthorizedException("Doctor " + doctor.getName()
                    + " can't modify prescriptions written by another doctor.");
        }

        // Write a new prescription
        Prescription pUpdated = new Prescription(doctor.getId(), newMedicine, newDose, newTime);

        // Update the resident prescription
        r.updatePres(numberOrdered, pUpdated);

        // Update in database
        int residentId = getResidentId(r);
        if (residentId != -1) {
            // Use try-with-resources to automatically close the connection and statement
            try (Connection conn = CareHomeDatabase.connect();
                PreparedStatement pre = conn.prepareStatement(
                        "SELECT id FROM prescription WHERE resident_id = ? AND doctor_id = ? AND medicine = ? AND dose = ? AND time = ? LIMIT 1")) {

                // Bind each placeholder (?) in the SQL with the actual values
                pre.setInt(1, residentId);
                pre.setString(2, doctor.getId());
                pre.setString(3, oldPrescription.getMedicine());
                pre.setString(4, oldPrescription.getDose());
                pre.setString(5, oldPrescription.getTime());

                // Execute the SQL command to insert the new record into the database
                ResultSet rs = pre.executeQuery();

                // If found the prescription in the database
                if (rs.next()) {

                    // Get the prescription ID from the result
                    int prescriptionId = rs.getInt("id");

                    // Update this prescription record in the database
                    CareHomeDatabase.updatePrescription(prescriptionId, newMedicine, newDose, newTime);
                }
            } catch (SQLException e) {
                // Catch database related errors
                System.out.println("Failed to update prescription in database: " + e.getMessage());
            }

        }

        // Create log message + print to console
        String showlog = "Doctor " + doctor.getName() + " (" + doctor.getId() + ") updated prescription ["
                + numberOrdered + "] for resident " + r.getName() + " in bed " + bedId + ": " + pUpdated.getMedicine()
                + " (" + pUpdated.getDose() + ") at " + pUpdated.getTime();
        System.out.println(showlog);
        CareHome.createLog(showlog);
    }


    /**
     * Allows a Nurse to move a resident to a new bed
     * @param nurse The Nurse performing the action
     * @param residentName The name of the resident being moved
     * @param newBedId  The ID of the new bed to assign
     * @throws UnauthorizedException Only nurse can move beds
     * @throws NotWorkingException check nurse is working on that day or not
     * @throws IllegalArgumentException can't find the resident
     * @throws IllegalArgumentException can't find the bed
     * @throws BedOccupiedException check if the bed is occupied
     */

    public void nurseMoveBed(Nurse nurse, String residentName, int newBedId) {

        // Only nurse can move a resident
        if (nurse == null) {
            throw new UnauthorizedException("Only nurse can move beds.");
        }

        // Check nurse is on duty that day
        String today = java.time.LocalDate.now().getDayOfWeek().toString();
        if (!isWorking(nurse, today)) {
            throw new NotWorkingException("Nurse " + nurse.getName() + " is not working today (" + today + ")");
        }

        // Find the resident by name
        Resident r = findResidentByName(residentName);
        if (r == null) {
            // If no resident is found, then throw an error
            throw new IllegalArgumentException("Can't find this resident: " + residentName);
        }

        // Look for the new bed with given ID
        Bed newBed = findBedById(newBedId);

        // If no bed is found with that id, throw an error
        if (newBed == null) {
            throw new IllegalArgumentException("Can't find the bed: " + newBedId);
        }

        // Check if the new bed already has a resident
        if (!newBed.bedAvailable()) {
            throw new BedOccupiedException("Bed: " + newBedId + " has a resident already!!");
        }

        // Find the old bed where the resident currently lives
        Bed rOldBed = null;
        if (r.getBedId() != null) {
            rOldBed = findBedById(r.getBedId());
        }

        // If the resident had an old bed, remove them from it
        if (rOldBed != null) {
            rOldBed.removeResident();
            rOldBed.setAvailable(true);

            // Update the old bed in database (is available)
            CareHomeDatabase.updateBed(rOldBed.getBedId(), true, null);
        }

        // Put the resident into the new bed
        newBed.assignResident(r);
        newBed.setAvailable(false);

        // Update the resident's bedID
        r.setBedId(newBedId);

        // Update the new bed in database (not available)
        CareHomeDatabase.updateBed(newBedId, false, getResidentId(r));

        int oldBedId;
        if (rOldBed == null) {
            oldBedId = -1;  // -1 means there is no old bed
        } else {
            oldBedId = rOldBed.getBedId();
        }

        // Create log message + print to console
        String showlog = "Nurse " + nurse.getName() + " moved resident " + r.getName() + " from bed "
                + oldBedId + " to bed" + newBedId;
        System.out.println(showlog);
        CareHome.createLog(showlog);

    }


    /**
     * Medical staff check the details of a resident in a bed
     * @param staff The medical staff performing the check
     * @param bedId The ID of the bed to check
     */

    public void checkDetailOfResidentInBed(Staff staff, int bedId) {

        // Check is the staff a medical staff
        if (!(staff instanceof Doctor) && !(staff instanceof Nurse)) {
            throw new UnauthorizedException("Only medical staff can check the details of resident in bed");
        }

        // Found the bed
        Bed theBed = findBedById(bedId);
        // If didnt find the bed
        if (theBed == null) {
            throw new IllegalArgumentException("Can't find bed: " + bedId);
        }

        // See if there is a resident on the bed
        Resident r = theBed.getResident();
        if (r == null) {
            System.out.println("Bed " + bedId + " is empty.");
            return;
        }

        // Print out resident info
        System.out.println("~~~~ Resident Details (Bed " + bedId + ") ~~~~");
        System.out.println("Name: " + r.getName());
        System.out.println("Gender: " + r.getGender());
        System.out.println("Bed ID: " + bedId);

        // Show Prescription
        if (r.getPrescriptions().isEmpty()) {
            System.out.println("Prescriptions are empty.");
        } else {
            System.out.println("Prescriptions: ");
            for (int i = 0; i < r.getPrescriptions().size(); i++) {
                Prescription p = r.getPrescriptions().get(i);
                System.out.println((i + 1) + ". " + p.getMedicine() + " (" + p.getDose()
                + ") at " + p.getTime() + " [Doctor ID: " + p.getDoctorId() + "]");
            }

        // Create log message + print to console
        String showlog = staff.getClass().getSimpleName() + " " + staff.getName() + " (" +
                staff.getId() + ") " + "checked the details of bed "
                + bedId + " with resident " + r.getName() + " (" + r.getGender() + ")";
        System.out.println(showlog);
        CareHome.createLog(showlog);
        }

    }


    /**
     * Allows a Doctor or Nurse to administer a prescription to a resident.
     * Nurses must be on a shift to administer.
     * @param staff The staff member performing the action
     * @param residentName The name of the resident receiving the medicine
     * @param numberOrdered The index of the prescription to administer
     * @throws IllegalArgumentException if the resident or prescription does not exist,
     * or if the staff is not authorized
     * @throws UnauthorizedException only doctor or nurse can administer prescriptions
     */

    public void adminPres(Staff staff, String residentName, int numberOrdered) {

        // Check is the staff a medical staff
        if (!(staff instanceof Doctor) && !(staff instanceof Nurse)) {
            throw new UnauthorizedException("Only medical staff can administer prescriptions.");
        }

        // Look for the resident
        Resident r = findResidentByName(residentName);
        if (r == null) {
            throw new IllegalArgumentException("Can't find the resident: " + residentName);
        }

        // Does the prescription exist or not
        if (numberOrdered < 0 || numberOrdered >= r.getPrescriptions().size()) {
            throw new IllegalArgumentException("Can't find the prescription: " + numberOrdered);
        }
        Prescription p = r.getPrescriptions().get(numberOrdered);

        // Check nurse is on duty that day
        if (staff instanceof Nurse nurse) {
            String today = java.time.LocalDate.now().getDayOfWeek().toString();
                if (!isWorking(nurse, today)) {
                    throw new NotWorkingException("Nurse " + nurse.getName() + " is not working today (" + today + ")");
            }
        }

        // Create log message + print to console
        String showlog = staff.getClass().getSimpleName() + " " + staff.getId() + " "+ staff.getName() + " administered " + p.getMedicine() +
                " (" + p.getDose() + ") at " + p.getTime() + " to resident " + r.getName() + " (Bed " + r.getBedId() + ")";
        System.out.println(showlog);
        CareHome.createLog(showlog);
    }


    /**
     * Create a log entry
     * @param message The log message
     */

    public static void createLog(String message) {
        String timestamp = java.time.LocalDateTime.now().toString();
        String logEntry = "[" + timestamp + "] " + message;

        // Print to console
        System.out.println(logEntry);

        // Save to memory list
        logged.add(logEntry);

        // Try extract staffId (if exists)
        // If message = "Manager Edward (M1) added a new staff,
        // then staffId = "M1"
        String staffId = null;
        if (message.contains("(") && message.contains(")")) {
            try {
                // Get the substring between "(" and ")"
                staffId = message.substring(message.indexOf("(") + 1, message.indexOf(")"));
            } catch (Exception ignore) {}
        }

        // Save to database
        CareHomeDatabase.insertLog(staffId, message);
    }


    // Print out all the logs we have in the system
    public void printAllLogs() {
        System.out.println("\nAll the logs:");
        for (String s : logged) {
            System.out.println(s);
        }
    }


    public Staff login(String id, String password) {
        for (Staff s : staffList) {
            if (s.getId().equals(id)) {
                if (s.getPassword().equals(password)) {
                    System.out.println("Login successful: " + s.getName() + " (" + s.getClass().getSimpleName() + ")");
                    return s;
                } else {
                    throw new IllegalArgumentException("Invalid password for staff id: " + id);
                }
            }
        }
        throw new IllegalArgumentException("Staff ID not found: " + id);
    }

    /**
     * Find a bed by its ID.
     * shorten codes to make the code looks clean
     * @param bedId the bed number to find
     * @return the Bed object if found, otherwise null
     */

    private Bed findBedById(int bedId) {
        for (Bed b : beds) {
            if (b.getBedId() == bedId) {
                return b; // If found then return bed object
            }
        }
        return null;
    }

    /**
     * Assign new shifts to staffs
     * Each day can't work over 8 hrs
     * @param m who assign new shift
     * @param s who get assign new shift
     * @param newShift shifts assigned
     */

    public void assignShift(Manager m, Staff s, Shift newShift) {
        if (m == null) {
            throw new UnauthorizedException("Only manager can assign shifts");
        }
        if (s == null || newShift == null) {
            throw new IllegalArgumentException(("Staff or shift can't be null"));
        }

        // Calculate total hours in that day
        int totalHours = 0;
        for (Shift shift : s.getShifts()) {
            if (shift.getDay().equalsIgnoreCase(newShift.getDay())) {
                totalHours += shift.getDuration();
            }
        }
        // Add shift duration into totalHours
        totalHours += newShift.getDuration();

        // If over 8 hrs shows error message
        if (totalHours > 8) {
            throw new IllegalArgumentException("Working hours can't be over 8 hours per day: now is " + totalHours + "hours");
        }

        // Add shift to staff object
        s.getShifts().add(newShift);

        // Save to database
        CareHomeDatabase.insertShift(s.getId(), newShift.getDay(), newShift.getTime());

        // Create a log + print to console
        String showlog = ("Manager " + m.getName() + " (" + m.getId() + ") " + " has assigned shift to "
        + s.getName() + " (" + s.getId() + ") " + " (" + newShift.toString() + ")");
        System.out.println(showlog);
        CareHome.createLog(showlog);
    }

    /**
     * Check is the staff working on that day
     * @param s who to check
     * @param day day to check
     * @return if is working on that day return true, if not return false
     */

    public boolean isWorking(Staff s, String day) {
        if (s == null || day == null) return false;

        for (Shift shift : s.getShifts()) {
            if (shift.getDay().equalsIgnoreCase(day)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Export all system logs to a text file
     */

    public void exportLogs() {

        //Generate date in the file name
        String filename = "care_home_logs_" + java.time.LocalDate.now() + ".txt";

        // Skip if no logs
        if (logged.isEmpty()) {
            System.out.println("No logs to export");
            return;
        }

        // Write the file
        try (FileWriter w = new FileWriter(filename, false)) {
            w.write("~~~~ CareHome System Log Export ~~~~" + System.lineSeparator());
            w.write("Exported at: " + java.time.LocalDateTime.now() + System.lineSeparator());
            w.write("----------------------------------------" + System.lineSeparator());

            // All the logs in the system
            for (String log : logged) {
                w.write(log + System.lineSeparator());
            }

            w.write(System.lineSeparator());
            System.out.println("Logs successfully exported to " + filename);

            // Clean up the logs in the memory after export
            logged.clear();
            System.out.println("All the logs in the memory has been cleared.");

        } catch (IOException e) {
            System.out.println("Unsuccessful writing logs to file: " + e.getMessage());
        }
    }

    /**
     * Find the database ID of a given resident by their name
     * @param r The resident
     * @return The resident's database ID, -1 if not found
     */

    private int getResidentId(Resident r) {
        String sql = "SELECT id FROM resident WHERE name = ?";

        // Use try-with-resources to automatically close the connection and statement
        try (Connection conn = CareHomeDatabase.connect();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            // Bind each placeholder (?) in the SQL with the actual values
            pre.setString(1, r.getName());

            // Run query and read the first matching record
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                // Return the resident's database ID
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            // Catch database related errors
            System.out.println("Cannot get resident ID from DB: " + e.getMessage());
        }
        return -1; // not found
    }

    /**
     * Allow a doctor to delete a prescription
     * @param doctor The doctor performing the action
     * @param bedId The bed ID of the resident
     * @param numberOrdered The index of the prescription to delete
     */

    public void docDeletePres(Doctor doctor, int bedId, int numberOrdered) {

        // Only doctor can perform this action
        if (doctor == null) {
            throw new UnauthorizedException(("Only doctor can delete prescriptions"));
        }

        // Find the bed, must not be null
        Bed bed = findBedById(bedId);
        if (bed == null) {
            throw new IllegalArgumentException("Can't find the bed: " + bedId);
        }

        // Find the resident, must not be null
        Resident r = bed.getResident();
        if (r == null) {
            throw new IllegalArgumentException("No resident in bed " + bedId);
        }

        // Check if index valid
        if (numberOrdered < 0 || numberOrdered >= r.getPrescriptions().size()) {
            throw new IllegalArgumentException("Prescription index out of range: " + numberOrdered);
        }

        // Get the prescription
        Prescription p = r.getPrescriptions().get(numberOrdered);

        // Is it the same doctor
        if (!p.getDoctorId().equals(doctor.getId())) {
            throw new UnauthorizedException("Doctor ID: " + doctor.getId() + " doesn't match the prescription ID: " + p.getDoctorId());
        }

        // Remove from memory
        r.getPrescriptions().remove(numberOrdered);

        // Remove from database
        int residentId = getResidentId(r);

        // Only proceed if the resident ID is valid (not -1)
        if (residentId != -1) {
            try (Connection conn = CareHomeDatabase.connect();
                PreparedStatement pre = conn.prepareStatement(
                        "DELETE FROM prescription WHERE resident_id = ? AND doctor_id = ? AND medicine = ? AND dose = ? AND time = ?")) {

                // Bind each placeholder (?) in the SQL with the actual values
                pre.setInt(1, residentId);
                pre.setString(2, doctor.getId());
                pre.setString(3, p.getMedicine());
                pre.setString(4, p.getDose());
                pre.setString(5, p.getTime());

                // Execute the SQL command to insert the new record into the database
                int executeUpdate = pre.executeUpdate();

                // Log to console
                if (executeUpdate > 0) {
                    // Print confirmation message
                    System.out.println("Prescription deleted successfully from database: " +
                            p.getMedicine() + " (" + p.getDose() + ") for resident " + r.getName());
                } else {
                    System.out.println("Can't find the matching record.");
                }
            } catch (SQLException e) {
                // Catch database related errors
                System.out.println("Failed to delete prescription from database: " + e.getMessage());
            }
        }

        // Create a log + print to console
        String showlog = "Doctor " + doctor.getName() + " deleted prescription " +
                p.getMedicine() + " for resident " + r.getName() + " (bed " + bedId + ")";
        System.out.println(showlog);
        CareHome.createLog(showlog);

    }


}
