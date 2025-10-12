import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import edu.rmit.cosc1295.carehome.*;

public class CareHomeTest {

    // Duplicate staff ID throw error
    @Test
    @DisplayName("addStaff() should throw error if same ID is added twice")
    void addStaff_duplicateId_throwsError() {
        // Add objects
        CareHome c = new CareHome();

        // Create manager and nurse
        Manager manager = new Manager("manager1", "Edward", "0722");
        Nurse nurse = new Nurse("nurse1", "Qin", "1234");

        // Manager add new nurse
        assertDoesNotThrow(() -> c.addStaff(manager, nurse));

        // Add another nurse with same ID, it should fail
        Nurse dup = new Nurse("nurse1", "Mona", "6666");
        assertThrows(IllegalArgumentException.class,
                () -> c.addStaff(manager, dup),
                "Same ID should not be allowed");
        // Show success message
        System.out.println("successfully blocked the duplicate ID!");
    }

    // Allow to add multiple IDs
    @Test
    @DisplayName("addStuff() should allow multiple different IDs without throwing error")
    void addStaff_duplicateIds_throwError() {
        CareHome c = new CareHome();

        // Create a manager
        Manager manager = new Manager("manager1", "Edward", "0722");

        // Create two nurses with different IDs
        Nurse nurse = new Nurse("nurse1", "Qin", "1234");
        Nurse nurse2 = new Nurse("nurse2", "Mona", "6666");

        // Manager add 2 nurses
        assertDoesNotThrow(() -> {
            c.addStaff(manager, nurse);
            c.addStaff(manager, nurse2);
        }, "Different IDs should not cause any error");

        // Show success message
        System.out.println("Successfully added multiple different IDs without throwing error!");
    }

    // Not a manager but try to add new staff
    @Test
    @DisplayName("addStuff() should throw UnauthorizedException if non-manager tries to add staff")
    void addStaff_non_manager_throwUnauthorizedException() {
        CareHome c = new CareHome();

        //Create a nurse
        Nurse nurse = new Nurse("nurse1", "Qin", "1234");

        // Should throw error here, not a manager to add staff
        assertThrows(UnauthorizedException.class,
                () -> c.addStaff(null, nurse),
                "Only manager can add staff");

        // Show success message
        System.out.println("Successfully blocked the non-manager from adding staff!");
    }

    // When adding a staff should create a log
    @Test
    @DisplayName("addStaff() should create a log when successfully adding a new staff")
    void addStaff_createLog() {
        CareHome c = new CareHome();

        // Create manager and nurse
        Manager manager = new Manager("manager1", "Edward", "0722");
        Nurse nurse = new Nurse("nurse1", "Qin", "1234");

        // Add a nurse, should not throw any error
        assertDoesNotThrow(() -> c.addStaff(manager, nurse));

        // Check if the log list is empty or not
        assertFalse(CareHome.getLogs().isEmpty(), "Logs should not be empty after adding a staff");

        // Show success message
        System.out.println("Successfully created a new log!");
    }

    // Staff should be added to the staff list
    @Test
    @DisplayName("addStuff() should add new staff to the internal list")
    void addStaff_toList() {
        CareHome c = new CareHome();

        // Create manager and nurse
        Manager manager = new Manager("manager1", "Edward", "0722");
        Nurse nurse = new Nurse("nurse1", "Qin", "1234");

        // List size before adding
        int oldList = c.getStaffList().size();

        // Add a nurse, should not throw any error
        assertDoesNotThrow(() -> c.addStaff(manager, nurse));

        // Check list size increased by 1
        int newList = c.getStaffList().size();
        assertEquals(oldList + 1, newList, "Staff list size should increase by 1");

        // Show success message
        System.out.println("Successfully added staff to the internal list!");
    }

    // Doctor should be assigned a shift successfully
    @Test
    @DisplayName("assignShift() should assign a doctor a valid shift without any error")
    void assignShift_toDoctor_success() {
        CareHome c = new CareHome();

        // Create manager and doctor
        Manager manager = new Manager("manager1", "Edward", "0722");
        Doctor doctor = new Doctor("doctor1", "Ellen", "2222");

        // Add a doctor, should not throw any error
        assertDoesNotThrow(() -> c.addStaff(manager, doctor));

        // Create a shift for the doctor
        Shift shift = new Shift("Friday", "12:00-13:00");

        // Assign the shift without throwing any error
        assertDoesNotThrow(() -> doctor.addShift(shift),
                "Doctor should receive a valid shift");

        // Show success message
        System.out.println("Successfully assigned shift to a doctor!");
    }

    // If a shift is over 8 hours in one day should throw error
    @Test
    @DisplayName("assignShift() should throw error if a staff has more than 8 hours work in one day")
    void assignShift_over8Hours_throwError() {
        CareHome c = new CareHome();

        // Create manager and nurse
        Manager manager = new Manager("Manager1", "Edward", "0722");
        Nurse nurse = new Nurse("nurse1", "Qin", "1234");

        // Add a doctor, should not throw any error
        assertDoesNotThrow(() -> c.addStaff(manager, nurse));

        // Assign one shifts plus one more hour to the same staff
        Shift shift1 = new Shift("Friday", "07:00-15:00");
        Shift shift2 = new Shift("Friday", "17:00-18:00");

        // Assign the shift without throwing any error
        assertDoesNotThrow(() -> nurse.addShift(shift1));

        // Assign another shift, this should throw error
        assertThrows(NotWorkingException.class, () -> nurse.addShift(shift2),
                "nurse should not work more than 8 hours per day");

        // Show success message
        System.out.println("Successfully blocked over 8 hours shift");
    }

    // Nurse should be assigned valid shifts successfully
    @Test
    @DisplayName("assignShift() should allow nurse to receive shifts under 8 hours per day")
    void assignShift_toNurse_success() {
        CareHome c = new CareHome();

        // Create manager and nurse
        Manager manager = new Manager("Manager1", "Edward", "0722");
        Nurse nurse = new Nurse("nurse1", "Qin", "1234");

        // Add a nurse, should not throw any error
        assertDoesNotThrow(() -> c.addStaff(manager, nurse));

        // Assign two shifts that total hour under 8 hours
        Shift shift1 = new Shift("Friday", "07:00-13:00");
        Shift shift2 = new Shift("Friday", "15:00-16:00");

        // Should not throw any error since total hour is under 8 hours
        assertDoesNotThrow(() -> {
            nurse.addShift(shift1);
            nurse.addShift(shift2);
        }, "Nurse working under 8 hours should be allowed");

        // Confirm both shifts exist in nurse’s list
        assertEquals(2, nurse.getShifts().size(),
                "Nurse should have exactly 2 valid shifts assigned");

        // Show success message
        System.out.println("Successfully assigned shift to nurse within working limit!");
    }

    // CheckCompliance() should return false when a nurse is overwork
    @Test
    @DisplayName("checkCompliance() should return false if a nurse has more than 8 hours of shifts in a single day")
    void checkCompliance_returnsFalse() {
        CareHome c = new CareHome();

        // Create manager and nurse
        Manager manager = new Manager("manager1", "Edward", "0722");
        Nurse nurse = new Nurse("nurse1", "Qin", "1234");

        // Add a nurse, should not throw any error
        assertDoesNotThrow(() -> c.addStaff(manager, nurse));

        // Assigned normal shift + extra 1 hour
        Shift shift1 = new Shift("Friday", "07:00-15:00");
        Shift shift2 = new Shift("Friday", "18:00-19:00");

        // The second addShift should throw exception
        assertThrows(NotWorkingException.class,
                () -> {
                    nurse.addShift(shift1);
                    nurse.addShift(shift2);
                },
                "Nurse should not work more than 8 hours a day");

        // Show success message
        System.out.println("Successfully detected overworked nurse!");
    }

    // Doctor cannot have two shifts on the same day
    @Test
    @DisplayName("addShift() should throw NotWorkingException if a doctor has two shifts on the same day")
    void doctorTwoShifts_sameDay_throwError() {
        Doctor doctor = new Doctor("doctor", "Ellen", "2222");

        // Added two shifts on Friday
        Shift shift1 = new Shift("Friday", "09:00-10:00");
        Shift shift2 = new Shift("Friday", "11:00-12:00");

        // The second addShift should throw exception
        assertThrows(NotWorkingException.class,
                () -> {
                    doctor.addShift(shift1);
                    doctor.addShift(shift2);
                },
                "Doctor should not have more than one shift per day");

        // Show success message
        System.out.println("Successfully blocked doctor from working twice on the same day!");
    }

    // Doctor's shift must be exactly 1 hour long
    @Test
    @DisplayName("addShift() should throw NotWorkingException if a doctor's shift is not exactly 1 hour long")
    void doctorShift_notOneHour_throwError() {

        // Create a doctor
        Doctor doctor = new Doctor("doctor1", "Ellen", "2222");

        // Added 2 hours to shift
        Shift invalidShift = new Shift("Tuesday", "09:00-11:00");


    }


}
