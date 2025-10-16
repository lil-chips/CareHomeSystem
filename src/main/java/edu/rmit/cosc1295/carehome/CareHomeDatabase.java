package edu.rmit.cosc1295.carehome;

import java.io.File;
import java.sql.*;

/**
 * This handles the database connection
 * It will automatically create the database file if it does not exist
 */

public class CareHomeDatabase {

    // The database file name used for SQLite
    private static final String DB_FILE_NAME = "care_home.db";

    /**
     * Get the absolute file path of the SQLite database.
     * @return the absolute path to the database file
     */

    public static String getDbPath() {
        return new File(System.getProperty("user.dir"), DB_FILE_NAME).getAbsolutePath();
    }

    /**
     * Get the JDBC connection URL for the SQLite database.
     * @return the full JDBC URL
     */

    public static String getDbUrl() {
        return "jdbc:sqlite:" + getDbPath();
    }

    /**
     * Connect to SQLite database
     * @return A successfully connected object, if failed then null
     */

    public static Connection connect() {
        Connection conn = null; // Declare a variable to hold the connection
        try {
            // Try to establish a connection using the generated JDBC URL
            conn = DriverManager.getConnection(getDbUrl());

            // Print confirmation message (for debugging)
            System.out.println("Connected to database at: " + getDbPath());
        } catch (SQLException e) {
            // Handle database connection errors
            System.out.println("Failed to connect: " + e.getMessage());
        }
        // Return the connection object (or null if failed)
        return conn;
    }

    /**
     * Create all the required tables for the system
     */

    public static void createTables() {
        Connection conn = null; // Used to connect to the database
        Statement state = null; // Used to execute SQL commands

        try {
            // Connect to the database
            conn = connect();

            // Create a Statement object for executing SQL
            state = conn.createStatement();

            String createStaffTable = """
                CREATE TABLE IF NOT EXISTS staff (
                    id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    role TEXT NOT NULL,
                    password TEXT NOT NULL
                );
            """;

            String createResidentTable = """
                CREATE TABLE IF NOT EXISTS resident (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    gender TEXT NOT NULL,
                    bed_id INTEGER
                );
            """;

            String createBedTable = """
                CREATE TABLE IF NOT EXISTS bed (
                    bed_id INTEGER PRIMARY KEY,
                    is_available INTEGER NOT NULL,
                    resident_id INTEGER,
                    FOREIGN KEY (resident_id) REFERENCES resident(id) ON DELETE SET NULL
                );
            """;

            String createPrescriptionTable = """
                CREATE TABLE IF NOT EXISTS prescription (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    resident_id INTEGER NOT NULL,
                    doctor_id TEXT NOT NULL,
                    medicine TEXT NOT NULL,
                    dose TEXT NOT NULL,
                    time TEXT NOT NULL,
                    FOREIGN KEY (resident_id) REFERENCES resident(id) ON DELETE CASCADE,
                    FOREIGN KEY (doctor_id) REFERENCES staff(id)
                );
            """;

            String createLogsTable = """
                CREATE TABLE IF NOT EXISTS logs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    timestamp TEXT NOT NULL,
                    staff_id TEXT,
                    action TEXT NOT NULL,
                    FOREIGN KEY (staff_id) REFERENCES staff(id)
                );
            """;

            String createShiftTable = """
                CREATE TABLE IF NOT EXISTS shift (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    staff_id TEXT NOT NULL,
                    day TEXT NOT NULL,
                    time TEXT NOT NULL,
                    FOREIGN KEY (staff_id) REFERENCES staff(id)
                );
            """;

            // Execute all the SQL statement
            state.execute(createStaffTable);
            state.execute(createResidentTable);
            state.execute(createBedTable);
            state.execute(createPrescriptionTable);
            state.execute(createLogsTable);
            state.execute(createShiftTable);

            System.out.println("Successfully created tables!");

        } catch (Exception e) {
            // Handle any SQL errors
            System.out.println("Failed to create tables: " + e.getMessage());
        } finally {
            // Always close resources to prevent memory leaks
            try {
                if (state != null) state.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    /**
     * Insert a log record into the database
     * @param staffId The ID of the staff performing the action
     * @param action The action message
     */

    public static void insertLog(String staffId, String action) {
        String sql = "INSERT INTO logs (timestamp, staff_id, action) VALUES (?, ?, ?)";

        // Use try-with-resources to automatically close the connection and statement
        try (Connection conn = connect();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            // Generate timestamp
            String now = java.time.LocalDateTime.now().toString();

            // Bind each placeholder (?) in the SQL with the actual values
            pre.setString(1, now);
            if (staffId != null) {
                // If staffId is not null insert into the database
                // Otherwise insert a SQL NULL
                pre.setString(2, staffId);
            } else {
                pre.setNull(2, Types.VARCHAR);
            }
            pre.setString(3, action);

            // Execute the SQL command to insert the new record into the database
            pre.executeUpdate();

            // Print confirmation message
            System.out.println("Log saved to database: " + action);

        } catch (SQLException e) {
            // Catch database related errors
            System.out.println("Failed to insert log: " + e.getMessage());
        }
    }

    /**
     * Insert staff record into the database
     * @param id Staff ID
     * @param name Staff name
     * @param role Staff role
     * @param password Login password
     */

    public static void insertStaff(String id, String name, String role, String password) {
        String sql = "INSERT INTO staff (id, name, role, password) VALUES (?, ?, ?, ?)";

        // Use try-with-resources to automatically close the connection and statement
        try (Connection conn = connect();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            // Bind each placeholder (?) in the SQL with the actual values
            pre.setString(1, id);
            pre.setString(2, name);
            pre.setString(3, role);
            pre.setString(4, password);

            // Execute the SQL command to insert the new record into the database
            pre.executeUpdate();

            // Print confirmation message
            System.out.println("Staff saved to database: " + name + " (" + id + ")");

        } catch (SQLException e) {
            // Catch database related errors
            System.out.println("Failed to insert staff: " + e.getMessage());
        }
    }

    public static void insertResident(String name, String gender, Integer bedId) {
        String sql = "INSERT INTO resident (name, gender, bed_id) VALUES (?, ?, ?)";

        // Use try-with-resources to automatically close the connection and statement
        try (Connection conn = connect();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            // Bind each placeholder (?) in the SQL with the actual values
            pre.setString(1, name);
            pre.setString(2, gender);

            // If bed is not null insert into the database
            // Otherwise insert a SQL NULL
            if (bedId != null)
                pre.setInt(3, bedId);
            else
                pre.setNull(3, Types.INTEGER);

            // Execute the SQL command to insert the new record into the database
            pre.executeUpdate();

            // Print confirmation message
            System.out.println("Resident added to database: " + name);

        } catch (SQLException e) {
            // Catch database related errors
            System.out.println("Failed to insert resident: " + e.getMessage());
        }
    }

    /**
     * Insert a prescription record into the database
     * @param residentId resident ID
     * @param doctorId doctor ID
     * @param medicine medicine name
     * @param dose dosage
     * @param time Administration time
     */

    public static void insertPrescription(int residentId, String doctorId, String medicine, String dose, String time) {
        String sql = "INSERT INTO prescription (resident_id, doctor_id, medicine, dose, time) VALUES (?, ?, ?, ?, ?)";

        // Use try-with-resources to automatically close the connection and statement
        try (Connection conn = connect();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            // Bind each placeholder (?) in the SQL with the actual values
            pre.setInt(1, residentId);
            pre.setString(2, doctorId);
            pre.setString(3, medicine);
            pre.setString(4, dose);
            pre.setString(5, time);

            // Execute the SQL command to insert the new record into the database
            pre.executeUpdate();

            // Print confirmation message
            System.out.println("Prescription added → ResidentID: " + residentId +
                    ", Doctor: " + doctorId +
                    ", " + medicine + " (" + dose + ") at " + time);

        } catch (SQLException e) {
            // Catch database related errors
            System.out.println("Failed to insert prescription: " + e.getMessage());
        }
    }

    /**
     * Insert a new bed record into the database.
     * @param bedId The bed number
     * @param isAvailable Whether the bed is available (true = 1, false = 0)
     * @param residentId The ID of the resident
     */

    public static void insertBed(int bedId, boolean isAvailable, Integer residentId) {
        String sql = "INSERT OR IGNORE INTO bed (bed_id, is_available, resident_id) VALUES (?, ?, ?)";

        // Use try-with-resources to automatically close the connection and statement
        try (Connection conn = connect();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            // Bind each placeholder (?) in the SQL with the actual values
            pre.setInt(1, bedId);
            pre.setInt(2, isAvailable ? 1 : 0);

            // If resident ID is not null insert into the database
            // Otherwise insert a SQL NULL
            if (residentId != null)
                pre.setInt(3, residentId);
            else
                pre.setNull(3, Types.INTEGER);

            // Execute the SQL command to insert the new record into the database
            int rows = pre.executeUpdate();

            // If rows > 0, insertion succeeded; otherwise skip (already exists)
            if (rows > 0) {
                System.out.println("Bed inserted into database: ID " + bedId +
                        ", Available=" + isAvailable +
                        (residentId != null ? ", ResidentID=" + residentId : ", (no resident)"));
            } else {
                System.out.println("Bed already exists, skipped: ID " + bedId);
            }

        } catch (SQLException e) {
            // Handle any SQL or connection error
            System.out.println("Failed to insert bed: " + e.getMessage());
        }
    }

    /**
     * Update the bed record in database when resident move in or out
     * @param bedId the bed ID to update
     * @param isAvailable bed is available or not
     * @param residentId the ID of the resident in bed
     */
    public static void updateBed(int bedId, boolean isAvailable, Integer residentId) {
        String sql = "UPDATE bed SET is_available = ?, resident_id = ? WHERE bed_id = ?";

        // Use try-with-resources to automatically close the connection and statement
        try (Connection conn = connect();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            // Bind each placeholder (?) in the SQL with the actual values
            pre.setInt(1, isAvailable ? 1 : 0);

            // If resident ID is not null insert into the database
            // Otherwise insert a SQL NULL
            if (residentId != null)
                pre.setInt(2, residentId);
            else
                pre.setNull(2, Types.INTEGER);

            // Target bed ID
            pre.setInt(3, bedId);

            // Execute the SQL command to insert the new record into the database
            int executedUpdate = pre.executeUpdate();

            // Log to console
            if (executedUpdate > 0) {
                // Print confirmation message
                String message = "Bed updated in database: ID:" + bedId + ", Available:" + isAvailable;

                // If there is a resident assigned
                if (residentId != null) {
                    message += ", ResidentID:" + residentId;
                } else {
                    message += ", (no resident)";
                }

                // Print confirmation
                System.out.println(message);

            } else {
                System.out.println("No bed found in database with ID: " + bedId);
            }

        } catch (SQLException e) {
            // Catch database related errors
            System.out.println("Failed to update bed: " + e.getMessage());
        }
    }

    /**
     * Update prescription in database (medicine, dose, and time)
     * @param prescriptionId the ID of the prescription
     * @param newMedicine the new medicine name
     * @param newDose  the new dosage
     * @param newTime the new administration time
     */
    public static void updatePrescription(int prescriptionId, String newMedicine, String newDose, String newTime) {
        String sql = "UPDATE prescription SET medicine = ?, dose = ?, time = ? WHERE id = ?";

        // Use try-with-resources to automatically close the connection and statement
        try (Connection conn = connect();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            // Bind each placeholder (?) in the SQL with the actual values
            pre.setString(1, newMedicine);
            pre.setString(2, newDose);
            pre.setString(3, newTime);
            pre.setInt(4, prescriptionId); // Target record

            // Execute the SQL command to insert the new record into the database
            int executedUpdate = pre.executeUpdate();

            // Log to console
            if (executedUpdate > 0)
                // Print confirmation message
                System.out.println("Prescription updated in database: ID:" + prescriptionId +
                        ", Medicine:" + newMedicine + ", Dose:" + newDose + ", Time:" + newTime);
            else
                System.out.println("No prescription found with ID: " + prescriptionId);

        } catch (SQLException e) {
            // Catch database related errors
            System.out.println("Failed to update prescription: " + e.getMessage());

        }
    }

    /**
     * Insert a new shift record into the database
     * @param staffId The staff ID
     * @param day The day of the shift
     * @param time The time range of the shift
     */

    public static void insertShift(String staffId, String day, String time) {
        String sql = "INSERT INTO shift (staff_id, day, time) VALUES (?, ?, ?)";

        // Use try-with-resources to automatically close the connection and statement
        try (Connection conn = connect();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            // Bind each placeholder (?) in the SQL with the actual values
            pre.setString(1, staffId);
            pre.setString(2, day);
            pre.setString(3, time);

            // Execute the SQL command to insert the new record into the database
            pre.executeUpdate();

            // Print confirmation message
            System.out.println("Shift inserted into database: " + staffId + ", " + day + ", " + time);

        } catch (SQLException e) {
            // Catch database related errors
            System.out.println("Failed to insert shift: " + e.getMessage());
        }
    }

    /**
     * Delete a prescription from the database by its ID
     * @param prescriptionId The ID of the prescription
     */

    public static void deletePrescription(int prescriptionId) {
        String sql = "DELETE FROM prescription WHERE id = ?";

        // Use try-with-resources to automatically close the connection and statement
        try (Connection conn = connect();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            // Bind each placeholder (?) in the SQL with the actual values
            pre.setInt(1, prescriptionId);

            // Execute the SQL command to delete the prescription in the database
            int deletePres = pre.executeUpdate();

            if ( deletePres > 0) {
                // Print confirmation message
                System.out.println("Prescription deleted from database: " + prescriptionId);
            } else {
                System.out.println("Can't not find prescription with ID: " + prescriptionId);
            }
        } catch (SQLException e) {
            // Catch database related errors
            System.out.println("Failed to delete prescription: " + e.getMessage());
        }
    }

    /**
     * Check if a staff with the given ID already exists in the database.
     * @param id The staff ID to check
     * @return true if the staff exists in the database, false otherwise
     */

    public static boolean staffExists(String id) {
        String sql = "SELECT 1 FROM staff WHERE id = ?";

        // Use try-with-resources to automatically close the connection and statement
        try (Connection conn = connect();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            // Bind each placeholder (?) in the SQL with the actual values
            pre.setString(1, id);

            // Execute the SQL command to check if this staff already exists
            ResultSet rs = pre.executeQuery();

            // If is true, staff already exists in the database
            return rs.next();

        } catch (SQLException e) {
            // Catch database related errors
            System.out.println("Failed to check staff existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clean up the staff records in database
     */

    public static void cleanAllStaff() {
        String sql = "DELETE FROM staff";

        // Use try-with-resources to automatically close the connection and statement
        try (Connection conn = connect();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            // Execute the SQL command to delete the staff record into the database
            pre.executeUpdate();

            // Print confirmation message
            System.out.println("Clean up all staff records for testing.");
        } catch (SQLException e) {
            // Catch database related errors
            System.out.println("Failed to clean staff table: " + e.getMessage());
        }
    }

    /**
     * Initialize the database by creating all required tables if they do not exist.
     * This is used for testing and first-time setup.
     */

    public static void initializeDatabase() {
            // Get the absolute database path (under the project directory)
            String dbPath = System.getProperty("user.dir") + File.separator + "care_home.db";

            // Build the full JDBC connection URL for SQLite
            String url = "jdbc:sqlite:" + dbPath;

            // Use try-with-resources to automatically close the connection and statement
            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {

                // Print confirmation message for debugging/logging
                System.out.println("Database initialized at: " + dbPath);

            // Create staff table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS staff (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                role TEXT NOT NULL,
                password TEXT NOT NULL
            )
        """);

            // Create resident table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS resident (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                gender TEXT,
                bed_id INTEGER
            )
        """);

            // Create bed table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS bed (
                bed_id INTEGER PRIMARY KEY,
                is_available INTEGER NOT NULL,
                resident_id INTEGER,
                FOREIGN KEY (resident_id) REFERENCES resident(id) ON DELETE SET NULL
            );
        """);

            // Create prescription table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS prescription (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                resident_id INTEGER NOT NULL,
                doctor_id TEXT NOT NULL,
                medicine TEXT NOT NULL,
                dose TEXT NOT NULL,
                time TEXT NOT NULL,
                FOREIGN KEY (resident_id) REFERENCES resident(id) ON DELETE CASCADE,
                FOREIGN KEY (doctor_id) REFERENCES staff(id)
            );
        """);

            // Create logs table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                timestamp TEXT NOT NULL,
                staff_id TEXT,
                action TEXT NOT NULL,
                FOREIGN KEY (staff_id) REFERENCES staff(id)
            );
        """);

            // Create shift table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS shift (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                staff_id TEXT NOT NULL,
                day TEXT NOT NULL,
                time TEXT NOT NULL,
                FOREIGN KEY (staff_id) REFERENCES staff(id)
            );
        """);

            System.out.println("Database initialized with all required tables.");

        } catch (SQLException e) {
                // Print detailed error message if database initialization fails
            System.out.println("Failed to initialize database: " + e.getMessage());
        }
    }

}
