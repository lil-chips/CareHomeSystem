package edu.rmit.cosc1295.carehome;

import java.sql.*;

/**
 * This handles the database connection
 * It will automatically create the database file if it does not exist
 */

public class CareHomeDatabase {

    private static final String DB_URL = "jdbc:sqlite:care_home.db"; // file name

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
    public static void createTable() {
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
    }
}
