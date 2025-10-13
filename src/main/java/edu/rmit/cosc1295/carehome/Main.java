package edu.rmit.cosc1295.carehome;

public class Main {

    public static void main(String[] args) {

        // Load saved data
        CareHome home = CareHome.loadFromFile("SavedData.ser");

        // If no file, create a new one
        if (home == null) {
            home = new CareHome();
            System.out.println("No data found. Creating a new one.");


            CareHomeDatabase.createTables();
            System.out.println("Database setup completed.");


        }
        Manager manager1 = new Manager("manager1", "Edward", "0722");
        Nurse nurse1 = new Nurse("nurse1", "Qin", "1234");
        home.addStaff(manager1, nurse1);

        home.saveToFile("SavedData.ser");
        // GUI
    }
}
