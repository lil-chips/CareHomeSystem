package edu.rmit.cosc1295.carehome;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


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
        for (int i = 0; i < staffList.size(); i++) {
            Staff s = staffList.get(i);
            if (s.getId().equals(newStaff.getId())) {
                throw new IllegalArgumentException("Staff id already exists: " + newStaff.getId());
            }
        }
        staffList.add(newStaff);
        System.out.println("Manager " + manager.getName() + " added a new staff: "
                + newStaff.getId() + " - " + newStaff.getName());

        // Create log message
        String showlog = "Manager " + manager.getName() + " add a new staff " + newStaff.getName() + "("
                + newStaff.getId() + ")";

        createLog(showlog);
    }


    /**
     * Print all staff in the list
     */

    public void showStaff() {
        if (staffList.isEmpty()) {
            System.out.println("No staff");
        } else {
            for (int i = 0; i < staffList.size(); i++) {
                Staff s = staffList.get(i);
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

        // Find the target bed
        Bed targetBed = null;
        for (Bed b : beds) {
            if (b.getBedId() == bedId) {
                targetBed = b;
                break;
            }
        }

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

        // Show the message
        System.out.println("Manager " + manager.getName() + " added a new resident: "
                + resident.getName() + " to bed " + bedId);

        // Create log message
        String showlog = "Manager " + manager.getName() + " (" + manager.getId() + ") added resident "
                + resident.getName() + " to bed " + bedId;

        createLog(showlog);
    }

    public void moveResident(Nurse nurse, String residentName, int newBedId) {
        if (nurse == null) {
            throw new IllegalArgumentException("Only nurses can move residents");
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

    }

    /**
     * Print out all the resident's name, gender and bed condition
     */

    public void printAllResidents() {
        if (residents.isEmpty()) {
            System.out.println("No residents");
        } else {
            for (int i = 0; i < residents.size(); i++) {
                Resident resident = residents.get(i);
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
        for (int i = 0; i < residents.size(); i++) {
            Resident re = residents.get(i);
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
        for (int i = 0; i < beds.size(); i++) {
            if (beds.get(i).getBedId() == bed.getBedId()) {
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

        for (int i = 0; i < beds.size(); i++) {
            Bed b = beds.get(i);
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
     * @param manager Who perform the action
     * @param residentName The name of the resident
     * @param bedId The ID of the bed
     * @throws IllegalArgumentException if the bed or resident does not exist
     * @throws IllegalStateException if the bed is already occupied or resident already has a bed
     */

    public void assignResidentToBed(Manager manager, String residentName, int bedId) {
        // Find the bed, set it to null first
        Bed targetbed = null;

        // If the target bed's bed ID is the same as one in the bed list
        // Then save it into targetbed and stop searching
        for (int i = 0; i < beds.size(); i++) {
            Bed b = beds.get(i);
            if (b.getBedId() == bedId) {
                targetbed = b;
                break;
            }
        }

        // If can't find the bed ID, then throw error
        if (targetbed == null) {
            throw new IllegalArgumentException("Can't find bed ID" + bedId);
        }

        // Bed must be empty
        // If bedAvailable == false, which means is occupied
        if (targetbed.bedAvailable() == false) {
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

        System.out.println("Assigned " + re.getName() + " to BedNo. " + bedId + " by " + manager.getName());

        // Create log message
        String showlog = "Manager " + manager.getName() + " (" + manager.getId() + ") assigned resident " + re.getName() +
                " to bed " + bedId;

        createLog(showlog);
    }


    /**
     * This loop can arrange shifts for nurses
     * Currently assigns 2 shifts per day (14 shifts per week).
     * Later have to change it to maximum 1 shift/day
     */

    public void allocateNursesShifts() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] nurseShiftTime = {"08:00-16:00", "14:00-22:00"};

        // Search the whole staff list
        for (int i = 0; i < staffList.size(); i++) {
            Staff staff = staffList.get(i);

            // If the staff is a nurse then run the loop
            if (staff instanceof Nurse nurse) {

                for (String day : days) {
                    for (String time : nurseShiftTime) {
                        Shift shift = new Shift(day, time);
                        nurse.addShift(shift);
                    }
                }

                System.out.println("Assigned 14 shifts to Nurse: " + nurse.getName());

                // Create log message
                String showlog = "Manager assigned 14 shifts to Nurse "
                        + nurse.getName() + "(" + nurse.getId() + ")";

                createLog(showlog);

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

        for (int i = 0; i < staffList.size(); i++) {
            Staff staff = staffList.get(i);

            if (staff instanceof Doctor doctor) {

                for (String day : days) {
                    Shift shift = new Shift(day, doctorShiftTime);
                    doctor.addShift(shift);
                }

                System.out.println("Assigned 7 shifts to Doctor: " + doctor.getName());

                // Create log message
                String showlog = "Manager assigned 7 shifts to doctor "
                        + doctor.getName() + "(" + doctor.getId() + ")";

                createLog(showlog);
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

                for (int j = 0; j < shifts.size(); j++) {
                    System.out.println("  " + shifts.get(j));
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

        for (int i = 0; i < staffList.size(); i++) {
            Staff staff = staffList.get(i);

            if (staff instanceof Doctor doctor) {

                System.out.println("Shifts for " + doctor.getName() + ":");

                ArrayList<Shift> shifts = doctor.getShifts();

                for (int j = 0; j < shifts.size(); j++) {
                    System.out.println(shifts.get(j));
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
        for (int i = 0; i < staffList.size(); i++) {
            Staff staff = staffList.get(i);

            if (staff instanceof Nurse nurse) {
                ArrayList<Shift> shifts = nurse.getShifts();

                HashMap<String, Integer> shiftMap = new HashMap<>();

                for (int j = 0; j < shifts.size(); j++) {
                    Shift shift = shifts.get(j);
                    String whichDay = shift.getDay(); // Get the day
                    int workingTime = shift.getShiftDuration();

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

        // Create log message
        String showlog = "Manager " + manager.getName() + " has modified the shift for nurse "
                + nurse.getName() + "(" + nurse.getId() + ")" + " , from " + preShift.toString() + " to "
                + newShift.toString();

        createLog(showlog);

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

        // Create log message
        String showlog = "Manager " + manager.getName() + " has modified the shift for doctor "
                + doctor.getName() + "(" + doctor.getId() + ")" + " , from " + preShift.toString() + " to "
                + newShift.toString();

        createLog(showlog);

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
        try {
            CareHome home;
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename))) {
                home = (CareHome) input.readObject();
            }
            System.out.println("Loaded from " + filename);
            return home;
        } catch (IOException ioe) {
            System.err.println("Failed to load due to IO error: " + ioe.getMessage());
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Failed to load due to missing class: " + cnfe.getMessage());
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

        Bed bed = null;
        for (int i = 0; i < beds.size(); i++) {
            Bed b = beds.get(i);
            if (b.getBedId() == bedId) {
                bed = b;
                break;
            }
        }

        // If bed is null then throw error
        if (bed == null) {
            throw new IllegalArgumentException("Can't find bed: " + bedId);
        }


        // Search the resident
        Resident r = bed.getResident();
        if (r == null) {
            throw new IllegalArgumentException("No resident in bed: " + bedId);
        }

        // Write a new prescription
        Prescription p = new Prescription(doctor.getId(), medicine, dose, time);

        // Add prescription into resident's prescription list
        doctor.addPresTo(r, p);

        // Print out the message
        System.out.println("Doctor " + doctor.getName() + " added prescription for resident "
                + r.getName() + " in bed " + bedId + ": " + medicine + " (" + dose + ") at " + time);

        // Create log message
        String showlog = "Doctor " + doctor.getName() + " (" + doctor.getId() + ") added prescription " + medicine +
                " (" + dose + ")" + " at " + time + " to " + r.getName();

        createLog(showlog);
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

        Bed bed = null;

        for (int i = 0; i < beds.size(); i++) {
            Bed b = beds.get(i);
            if (b.getBedId() == bedId) {
                bed = b;
                break;
            }
        }

        // If bed is null then throw error
        if (bed == null) {
            throw new IllegalArgumentException("Can't find bed: " + bedId);
        }
        // Search the resident
        Resident r = bed.getResident();
        if (r == null) {
            throw new IllegalArgumentException("No resident in bed: " + bedId);
        }

        // Write a new prescription
        Prescription pUpdated = new Prescription(doctor.getId(), newMedicine, newDose, newTime);

        // Update the resident prescription
        r.updatePres(numberOrdered, pUpdated);

        // Print out the message
        System.out.println("Doctor " + doctor.getName() + " updated prescription [" + numberOrdered
                + "] for resident " + r.getName() + " in bed " + bedId + ": " + pUpdated);

        // Create log message
        String showlog = "Doctor " + doctor.getName() + " updated prescription [" + numberOrdered + "] for resident "
                + r.getName() + " in bed " + bedId + " to " + pUpdated.toString();

        createLog(showlog);

    }


    /**
     * Allows a Nurse to move a resident to a new bed
     * @param nurse The Nurse performing the action
     * @param residentName The name of the resident being moved
     * @param newBedId  The ID of the new bed to assign
     */

    public void nurseMoveBed(Nurse nurse, String residentName, int newBedId) {

        // Find the resident by name
        Resident r = findResidentByName(residentName);
        if (r == null) {
            // If no resident is found, then throw an error
            throw new IllegalArgumentException("Can't find this resident: " + residentName);
        }

        // Look for the new bed with given ID
        Bed emptyBed = null;
        for (int i = 0; i < beds.size(); i++) {
            Bed b = beds.get(i);
            if (b.getBedId() == newBedId) {
                emptyBed = b;
                break; // stop once we find the bed
            }
        }

        // If no bed is found with that id, throw an error
        if (emptyBed == null) {
            throw new IllegalArgumentException("Can't find the bed: " + newBedId);
        }

        // Check if the new bed already has a resident
        if (emptyBed.getResident() != null) {
            throw new IllegalStateException("Bed: " + newBedId + " has a resident already!!");
        }

        // Find the old bed where the resident currently lives
        Bed rOldBed = null;
        for (int i = 0; i < beds.size(); i++) {
            Bed b = beds.get(i);
            if (r.getBedId() != null && b.getBedId() == r.getBedId()) {
                rOldBed = b;
                break;
            }
        }

        // If the resident had an old bed, remove them from it
        if (rOldBed != null) {
            rOldBed.removeResident();
            rOldBed.setAvailable(true);
        }

        // Put the resident into the new bed
        emptyBed.assignResident(r);
        emptyBed.setAvailable(false);

        // Update the resident's bedID
        r.setBedId(newBedId);

        System.out.println("Nurse " + nurse.getName() + " moved resident " + residentName + " to a new bed "
                + newBedId);

        // Create log message
        String showlog = "Nurse " + nurse.getName() + " moved resident " + residentName + " from bed "
                + emptyBed + " to " + "bed" + newBedId;

        createLog(showlog);

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
        Bed theBed = null;
        for (int i = 0; i < beds.size(); i++) {
            Bed b = beds.get(i);
            if (b.getBedId() == bedId) {
                theBed = b;
                break;
            }
        }

        // If didnt find the bed
        if (theBed == null) {
            throw new IllegalArgumentException("Can't find bed: " + bedId);
        }

        // See if there is a resident on the bed
        Resident r = theBed.getResident();
        if (r == null) {
            System.out.println("Bed " + bedId + " is empty.");
        } else {
            // Print out resident info
            System.out.println("Bed " + bedId + " has resident: " + r.getName()
                    + " (" + r.getGender() + ")");

            // Create log message
            String showlog = "Staff " + staff.getName() + " checked the details of bed "
                    + bedId + " with resident " + r.getName() + " (" + r.getGender() + ")";

            createLog(showlog);
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
     *
     */

    public void adminPres(Staff staff, String residentName, int numberOrdered) {

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

        // Check is the staff a medical staff
        if (!(staff instanceof Doctor) && !(staff instanceof Nurse)) {
            throw new UnauthorizedException("Only medical staff can administer prescriptions.");
        }

        // If is a nurse, Check the shift
        // Need to change it to day + time, not just shift
        if (staff instanceof Nurse nurse) {
            if (nurse.getShifts().isEmpty()) {
                throw new IllegalArgumentException("Our dear nurse " + nurse.getName() + " has no shifts today.");
            }
        }

        // Create log message
        String showlog = "Staff " + staff.getId() + " "+ staff.getName() + " administered " + p.getMedicine() +
                " (" + p.getDose() + ") at " + p.getTime() + " to resident " + r.getName();

        createLog(showlog);
    }


    // Log the actions
    public static void createLog(String log) {
        java.time.LocalDateTime currentTime = java.time.LocalDateTime.now();
        String combine = "[" + currentTime + "]" + log; // Add current time in the front
        logged.add(combine);
    }


    // Print out all the logs we have in the system
    public void printAllLogs() {
        System.out.println("\nAll the logs:");
        for (int i = 0; i < logged.size(); i++) {
            System.out.println(logged.get(i));
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




}
