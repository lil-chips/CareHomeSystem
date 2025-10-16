package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.Bed;
import edu.rmit.cosc1295.carehome.CareHome;
import edu.rmit.cosc1295.carehome.Doctor;
import edu.rmit.cosc1295.carehome.Staff;
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

/**
 * Only doctors can perform this action.
 */

public class DeletePrescriptionController {

    @FXML private ChoiceBox<Integer> bedChoice;
    @FXML private TextField indexField;
    @FXML private Button deleteBtn;
    @FXML private Button backBtn;

    private CareHome model;
    private Staff loggedInStaff;

    /**
     * Let the dashboard receive the shared CareHome model
     * Gives the controller access to all system data
     * @param model The shared CareHome object that holds all data
     */

    public void setModel(CareHome model) {
        // Store the shared CareHome instance for data access
        this.model = model;

        // Fill all existing bed IDs
        for (Bed b : model.getBeds()) {
            if (!b.bedAvailable()) {
                // show only occupied beds
                bedChoice.getItems().add(b.getBedId());
            }
        }

        // If there are occupied beds, pre-select the first one
        if (!bedChoice.getItems().isEmpty()) {
            bedChoice.setValue(bedChoice.getItems().getFirst());
        }
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
     * Called when "Delete" is clicked.
     */

    @FXML
    void onDeletePrescription(ActionEvent event) {
        try {
            // Verify authorization — only doctors can delete prescriptions
            if (!(loggedInStaff instanceof Doctor)) {
                showAlert("Only doctors can delete prescriptions!");
                return;
            }

            // Retrieve user input from the GUI
            Integer bedId = bedChoice.getValue(); // selected bed ID from dropdown
            String indexText = indexField.getText().trim(); // prescription index input

            // Validate inputs — both bed and index must be provided
            if (bedId == null || indexText.isEmpty()) {
                showAlert("Please select a bed and enter prescription index.");
                return;
            }

            // Convert prescription index to integer
            int index = Integer.parseInt(indexText);

            // Call backend logic to delete the prescription
            model.docDeletePres((Doctor) loggedInStaff, bedId, index);

            // Notify the user of successful recording
            showAlert("Prescription deleted successfully!");
            // Go back to the dashboard page
            onBack(event);

        } catch (NumberFormatException e) {
            // Handle invalid number format for index
            showAlert("Index must be a number (0 for first prescription).");
        } catch (Exception e) {
            // Catch-all for unexpected errors
            showAlert("Failed to delete prescription: " + e.getMessage());
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
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/edu/rmit/cosc1295/ui/dashboard.fxml"));
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
            showAlert("Failed to go back: " + e.getMessage());
        }
    }

    /**
     * Helper function that shows an information pop-up message.
     * @param msg The message to display in the alert box
     */

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null); // We don’t need a title
        a.setContentText(msg); // Show our message
        a.showAndWait(); // Wait until the user closes it
    }
}
