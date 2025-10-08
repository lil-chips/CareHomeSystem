package edu.rmit.cosc1295.carehome;

import java.sql.*;

/**
 * This handles the database connection
 * It will automatically create the database file if it does not exist
 */

public class CareHomeDatabase {

    private static final String DB_URL = "jdbc:sqlite:/Users/edwardedward/Desktop/CareHomeSystem/care_home.db";


    /**
     * Connect to SQLite database
     * @return A successfully connected object, if failed then null
     */

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to database: " + DB_URL);
        } catch (SQLException e) {
            System.out.println("Failed to connect database: " + e.getMessage());
        }
        return conn;
    }

    /**
     * Create all the required tables for the system
     */
    public static void createTables() {
        Connection conn = null;
        Statement state = null;

        try {

            conn = connect();
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
                    FOREIGN KEY (resident_id) REFERENCES residents(id)
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
                    FOREIGN KEY (resident_id) REFERENCES residents(id),
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

            // Execute all the SQL statement
            state.execute(createStaffTable);
            state.execute(createResidentTable);
            state.execute(createBedTable);
            state.execute(createPrescriptionTable);
            state.execute(createLogsTable);

            System.out.println("Successfully created tables!");

        } catch (Exception e) {
            System.out.println("Failed to create tables: " + e.getMessage());
        } finally {
            try {
                if (state != null) state.close();
                if (conn != null) conn.close();
            } catch (Exception ignore) {}
        }
    }

    /**
     * Insert a log record into the database
     * @param staffId The ID of the staff performing the action
     * @param action The action message
     */

    public static void insertLog(String staffId, String action) {
        String sql = "INSERT INTO logs (timestamp, staff_id, action) VALUES (?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            // Generate timestamp
            String now = java.time.LocalDateTime.now().toString();

            pre.setString(1, now);
            pre.setString(2, staffId);
            pre.setString(3, action);

            pre.executeUpdate();
            System.out.println("Log saved to database: " + action);


        } catch (SQLException e) {
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

        try (Connection conn = connect();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setString(1, id);
            pre.setString(2, name);
            pre.setString(3, role);
            pre.setString(4, password);

            pre.executeUpdate();
            System.out.println("Staff saved to database: " + name + " (" + id + ")");
        } catch (SQLException e) {
            System.out.println("Failed to insert staff: " + e.getMessage());
        }
    }


}
