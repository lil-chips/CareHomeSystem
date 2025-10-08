package edu.rmit.cosc1295.carehome;

public class Main {

    public static void main(String[] args) {

        // Load saved data
        CareHome home = CareHome.loadFromFile("SavedData.ser");

        // If no file, create a new one
        if (home == null) {
            home = new CareHome();
            System.out.println("No data found. Creating a new one.");

            Manager mgr = new Manager("M1", "Manager One", "pw");
            home.addStaff(mgr, mgr);
            Nurse nurse = new Nurse("N1", "Nurse Lily", "pw");
            home.addStaff(mgr, nurse);
            home.assignShift(mgr, nurse, new Shift("MONDAY", "08:00-16:00"));


        }

        home.saveToFile("SavedData.ser");
        // GUI
    }
}
