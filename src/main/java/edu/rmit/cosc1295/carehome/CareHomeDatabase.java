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
}
