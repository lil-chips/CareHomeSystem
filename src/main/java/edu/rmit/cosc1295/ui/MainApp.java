package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.*;

/**
 * Entry point of the app
 */
public class MainApp extends Application {

    private CareHome model;
    private String loggedInId;
    private String loggedInRole;

    @Override
    public void start(Stage stage) throws Exception{
        CareHomeDatabase.createTables();
        model = CareHome.loadFromFile("SavedData.ser");
        if (model == null) {
            model = new CareHome();

            Manager manager1 = new Manager("manager1", "Edward", "0722");
            Nurse nurse1 = new Nurse("nurse1", "Qin", "1234");
            Doctor doctor1 = new Doctor("doctor1", "Eric", "0000");
            model.addStaff(manager1, nurse1);
            model.addStaff(manager1, manager1);

            System.out.println("No data found. Created new CareHome instance.");
        }

        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/login.fxml"));
        Scene scene = new Scene(loader.load(), 520, 360);

        // Get the controller object that is linked to login.fxml
        LoginController controller = loader.getController();
        // Give the controller access to the CareHome model
        controller.setModel(model);

        // Configure and show the stage
        stage.setTitle("CareHome System - Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // Save on exit
        stage.setOnCloseRequest(event -> {
            model.saveToFile("SavedData.ser");
            System.out.println("Data saved to SavedData.ser");
        });
    }

    // Accessors for login info
    public CareHome getModel() { return model; }
    public String getLoggedInId() { return loggedInId; }
    public void setLoggedInId(String id) { this.loggedInId = id; }
    public String getLoggedInRole() { return loggedInRole; }
    public void setLoggedInRole(String role) { this.loggedInRole = role; }


    // Launch JavaFX app
    public static void main(String[] args) {
        launch(args);
    }
}