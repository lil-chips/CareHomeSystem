package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddStaffController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField passwordField;
    @FXML private ChoiceBox<String> roleChoice;
    @FXML private Button addBtn;
    @FXML private Button backBtn;

    private CareHome model;
    private Staff loggedInStaff;

    /**
     * Initializes the Add Staff screen when it is first loaded.
     */

    @FXML
    public void initialize() {
        // Populate the role dropdown with available staff roles
        roleChoice.getItems().addAll("Manager", "Doctor", "Nurse");

        // Set a default selection (Nurse)
        roleChoice.setValue("Nurse");
    }

    /**
     * Sets the shared CareHome model for this controller.
     * @param model the shared CareHome system model
     */

    public void setModel(CareHome model) {
        this.model = model; // Assign the shared model reference
    }

    /**
     * Sets the currently logged-in staff member.
     * @param staff the Staff object representing the logged-in user
     */

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    /**
     * Handles the "Add Staff" button click event from the user interface.
     * @param event the button click event that triggered this action
     */

    @FXML
    void onAddStaff(ActionEvent event) {
        // Retrieve input values from the text fields and dropdown
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleChoice.getValue();

        // Validate mandatory input fields
        if (id.isEmpty() || name.isEmpty() || password.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        // Create the appropriate Staff subclass based on selected role
        try {
            Staff newStaff;
            switch (role) {
                case "Manager" -> newStaff = new Manager(id, name, password);
                case "Doctor" -> newStaff = new Doctor(id, name, password);
                case "Nurse"  -> newStaff = new Nurse(id, name, password);
                default -> throw new IllegalArgumentException("Unknown role: " + role);
            }

            // Add the new staff to the CareHome system, must be a Manager
            model.addStaff((Manager) loggedInStaff, newStaff);

            // Record this action in the system log
            CareHome.createLog("Manager " + loggedInStaff.getName() + " added new " + role + ": " + name);

            // Show success feedback to the user
            showAlert("Staff added successfully!");

            // Go back to the dashboard page
            onBack(event);

        } catch (Exception e) {
            // Catch-all for unexpected errors
            showAlert("Failed to add staff: " + e.getMessage());
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
            // Load the Dashboard FXML layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);

            // Pass data back to DashboardController
            DashboardController controller = loader.getController();
            controller.setModel(model);
            controller.setLoggedInStaff(loggedInStaff);

            // Replace current scene with dashboard
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Dashboard");
            stage.show();
        } catch (Exception e) {
            // Handle any loading or transition errors
            showAlert("Failed to return: " + e.getMessage());
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
        alert.showAndWait(); // Wait until the user closes it
    }
}

