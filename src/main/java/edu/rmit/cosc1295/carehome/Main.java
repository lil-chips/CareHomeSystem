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

        Manager manager = new Manager("m1", "Edward", "0722");
        Nurse nurse = new Nurse("n1", "Lucy", "1234");
        Doctor doctor = new Doctor("d1", "Ryan", "9999");

        home.addStaff(manager, nurse);
        home.addStaff(manager, doctor);

        home.saveToFile("SavedData.ser");
        // GUI
    }
}
