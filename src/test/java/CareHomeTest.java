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

        Manager manager = new Manager("manager1", "Edward", "0722");

        // Create a nurse
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

        Manager manager = new Manager("manager1", "Edward", "0722");

        // Create a nurse
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

        Manager manager = new Manager("manager1", "Edward", "0722");

        // Create a doctor
        Doctor doctor = new Doctor("doctor1", "Ellen", "2222");


    }
}
