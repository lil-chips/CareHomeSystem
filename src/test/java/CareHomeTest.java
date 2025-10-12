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


    @Test
    @DisplayName("addStuff() should allow multiple different IDs without throwing error")
    void addStaff_duplicateIds_throwError() {
        CareHome c = new CareHome();
        Manager manager = new Manager("manager1", "Edward", "0722");


    }
}
