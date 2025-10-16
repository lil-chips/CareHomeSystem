package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for MoveResident.fxml
 * Allows nurses to move a resident from one bed to another.
 */

public class MoveResidentController {

    @FXML private ChoiceBox<String> residentChoice;
    @FXML private ChoiceBox<Integer> bedChoice;
    @FXML private Button moveBtn;
    @FXML private Button backBtn;

    private CareHome model;
    private Staff loggedInStaff;

    /**
     * Receive shared CareHome model.
     * Populate the dropdowns with residents and available beds.
     * @param model The shared CareHome object that holds all data
     */

    public void setModel(CareHome model) {
        // Store reference to the shared model
        this.model = model;

        // Populate resident dropdown with all resident names
        residentChoice.getItems().clear(); // Clear any previous data
        for (Resident r : model.getResidents()) {
            residentChoice.getItems().add(r.getName());
        }

        // Populate bed dropdown with only empty beds
        bedChoice.getItems().clear();
        for (Bed b : model.getBeds()) {
            if (b.bedAvailable()) {
                bedChoice.getItems().add(b.getBedId());
            }
        }

        // Set default selections if lists are not empty
        if (!residentChoice.getItems().isEmpty())
            residentChoice.setValue(residentChoice.getItems().get(0));
        if (!bedChoice.getItems().isEmpty())
            bedChoice.setValue(bedChoice.getItems().get(0));

    }

    /**
     * Save info about who is logged in.
     * @param staff The logged-in nurse
     */

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    /**
     * Called when the nurse clicks the "Move" button.
     * @param event Button click event
     */

    @FXML
    void onMove(ActionEvent event) {
        // Only nurses can perform this action
        if (!(loggedInStaff instanceof Nurse)) {
            showAlert("Only nurses can move residents.");
            return;
        }

        // Get user selections from the dropdowns
        String residentName = residentChoice.getValue();
        Integer newBedId = bedChoice.getValue();

        // Both resident and new bed must be selected
        if (residentName == null || newBedId == null) {
            showAlert("Please select both a resident and a new bed.");
            return;
        }

        try {
            // Perform the move operation via the model
            model.moveResident((Nurse) loggedInStaff, residentName, newBedId);

            // Log the move for auditing purposes
            CareHome.createLog("Nurse " + loggedInStaff.getName() +
                    " moved " + residentName + " to bed " + newBedId);

            // Notify the user of successful recording
            showAlert("Resident moved successfully!");

            // Go back to the dashboard page
            onBack(event);

        } catch (NotWorkingException e) {
            // If nurse is not currently working show error message
            showAlert("!!! " + e.getMessage());
        } catch (Exception e) {
            // Catch-all for unexpected errors
            showAlert("Failed to move resident: " + e.getMessage());
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
        alert.setTitle("CareHome System");
        alert.setHeaderText(null); // We don’t need a title
        alert.setContentText(msg); // Show our message
        alert.showAndWait(); // Wait until the user closes it
    }
}
