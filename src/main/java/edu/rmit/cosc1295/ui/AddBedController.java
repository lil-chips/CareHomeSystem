package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller for AddBed.fxml
 * Allows manager to add a new bed into the system.
 */

public class AddBedController {

    @FXML
    private TextField bedIdField;
    private CareHome model;
    private Staff loggedInStaff;

    /**
     * Sets the shared CareHome model for this controller.
     * @param model the shared CareHome system model
     */

    public void setModel(CareHome model) {
        this.model = model;
    }

    /**
     * Sets the currently logged-in staff member.
     * @param staff the Staff object representing the logged-in user
     */

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    /**
     * Handles the "Add Bed" button click event from the user interface.
     * @param event the button click event that triggered this action
     */

    @FXML
    void onAddBed(ActionEvent event) {
        // Retrieve and trim user input from the text field
        String bedIdText = bedIdField.getText().trim();

        if (bedIdText.isEmpty()) {
            // check if input is empty
            showAlert("Please enter a Bed ID!");
            return;
        }

        try {
            // Convert input to integer
            int bedId = Integer.parseInt(bedIdText);

            // Create a new Bed object with the given ID
            Bed newBed = new Bed(bedId);

            // Add the new bed through the CareHome model, must be a manager
            model.addBed((Manager) loggedInStaff, newBed);

            // Log the action for system tracking
            CareHome.createLog("Manager " + loggedInStaff.getName() + " added new bed " + bedId);
            // Console output for debugging
            System.out.println("Bed " + bedId + " added successfully by " + loggedInStaff.getName());

            // Show success alert to the user
            showAlert("Bed added successfully!");

            // Return to the dashboard screen
            onBack(event);

        } catch (NumberFormatException e) {
            // Handle non-numeric input for bed ID
            showAlert("Bed ID must be a number!");
        } catch (UnauthorizedException e) {
            // Handle unauthorized attempts, only manager allowed
            showAlert("Only manager can add beds!");
        } catch (Exception e) {
            // Catch-all for any unexpected error
            showAlert("Failed to add bed: " + e.getMessage());
        }
    }

    /**
     * Go back to the dashboard screen.
     * @param event The button click event
     */

    @FXML
    void onBack(ActionEvent event) {
        try {
            // Load the Dashboard FXML layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);

            // Pass data back to DashboardController
            DashboardController controller = loader.getController();
            controller.setModel(model); // Share the CareHome model instance
            controller.setLoggedInStaff(loggedInStaff); // Keep the logged-in user info

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
