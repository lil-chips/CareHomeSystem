package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;


import java.awt.*;

public class AddResidentController {

    @FXML
    private TextField nameField;

    @FXML
    private ChoiceBox<String> genderChoice;

    @FXML
    private ChoiceBox<Integer> bedChoice;

    private CareHome model;
    private Staff loggedInStaff;

    /**
     * Prepare the gender options for the dropdown
     */

    @FXML
    public void initialize() {
        // Fill gender options
        genderChoice.getItems().addAll("Male", "Female", "Other");
        genderChoice.setValue("Male");
    }

    /**
     * Let the dashboard receive the shared CareHome model
     * Gives the controller access to all system data
     * @param model The shared CareHome object that holds all data
     */

    public void setModel(CareHome model) {
        this.model = model;

        // Populate empty bed IDs
        bedChoice.getItems().clear();
        for (Bed b : model.getBeds()) {
            // Only show beds that are still available (not occupied)
            if (b.bedAvailable()) {
                bedChoice.getItems().add(b.getBedId()); // Add the bed ID to the dropdown list
            }
        }
        // If there are any available beds, automatically select the first one
        if (!bedChoice.getItems().isEmpty())
            bedChoice.setValue(bedChoice.getItems().get(0));
    }

    /**
     * Called from Dashboard to know who is logged in
     * Mainly for recording actions in the log
     * @param staff The staff member who is logged in
     */

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    /**
     * Triggered when clicks the "Add Resident" button
     * Checks the input and creates a new resident
     * Assigns the resident to a chosen bed, and shows a success message.
     * @param event The button click event
     */

    @FXML
    void onAddResident(ActionEvent event) {
        String name = nameField.getText().trim(); // Get name input
        String gender = genderChoice.getValue(); // Get gender selection
        Integer bedId = bedChoice.getValue(); // Get selected bed ID

        // Make sure all fields are filled in
        if (name.isEmpty() || bedId == null) {
            showAlert("Please enter resident info and select a bed.");
            return;
        }

        try {
            // Create a new resident and assign to the selected bed
            Resident newResident = new Resident(name, gender, bedId);
            model.assignResidentToBed(loggedInStaff, newResident, bedId);

            // Record this action in the system log
            CareHome.createLog(loggedInStaff.getName() + " added new resident " + name + " to bed " + bedId);

            // Inform the user that everything worked
            showAlert("Resident added successfully!");
            System.out.println("Resident added: " + name + " (" + gender + ") → Bed " + bedId);

            // Go back to the dashboard page
            onBack(event);

        } catch (Exception e) {
            // If something goes wrong, print the error details
            showAlert("Failed to add resident: " + e.getMessage());
        }
    }

    /**
     * Called when the user clicks the "Back" button.
     * This method switches the current scene back to the dashboard.
     * @param event The button click event used to get the current window
     */

    @FXML
    void onBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);

            // Pass data back to DashboardController
            DashboardController controller = loader.getController();
            controller.setModel(model);
            if (loggedInStaff != null)
                controller.setLoggedInStaff(loggedInStaff);

            // Replace current scene with dashboard
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Dashboard");
            stage.show();

        } catch (Exception e) {
            showAlert("Failed to return to dashboard: " + e.getMessage());
        }
    }

    /**
     * Helper function that shows an information pop-up message.
     * @param msg The message to display in the alert box
     */

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null); // We don’t need a title
        alert.setContentText(msg); // Show our message
        alert.showAndWait();       // Wait until the user closes it
    }
}



