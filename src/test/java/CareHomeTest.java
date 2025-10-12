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
}
