package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.CareHome;
import edu.rmit.cosc1295.carehome.Manager;
import edu.rmit.cosc1295.carehome.Staff;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * Allows the manager to reset a staff member’s password.
 */

public class ModifyPasswordController {

    @FXML private ChoiceBox<String> staffChoice;
    @FXML private PasswordField newPassField;
    @FXML private Button updateBtn;
    @FXML private Button backBtn;

    private CareHome model;
    private Staff loggedInStaff;

    /**
     * Called by Dashboard to share the data model.
     * It also fills the staff dropdown list.
     * @param model The shared CareHome model
     */

    public void setModel(CareHome model) {
        this.model = model;

        // Fill all staff members into dropdown
        staffChoice.getItems().clear();
        for (Staff s : model.getStaffList()) {
            staffChoice.getItems().add(s.getId() + " - " + s.getName());
        }

        if (!staffChoice.getItems().isEmpty())
            staffChoice.setValue(staffChoice.getItems().get(0));
    }

    /**
     * Called by Dashboard to record which manager is logged in.
     * @param staff The logged-in manager
     */

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    /**
     * When the manager clicks “Update Password” button.
     * @param event The button click event
     */
    @FXML
    void onUpdatePassword(ActionEvent event) {
        String selected = staffChoice.getValue();
        String newPassword = newPassField.getText();

        if (selected == null || newPassword.isBlank()) {
            showAlert("Please select a staff and enter a new password.");
            return;
        }
        try {
            String staffId = selected.split(" - ")[0];
            Staff target = model.findStaffById(staffId);

            if (target == null) {
                showAlert("Staff not found!");
                return;
            }

            ((Manager) loggedInStaff).modifyStaffPassword(target, newPassword);
            CareHome.createLog("Manager " + loggedInStaff.getName() + " changed password for " + target.getName());

            // Notify the user of successful recording
            showAlert("Password updated successfully!");

            // Go back to the dashboard page
            onBack(event);
        } catch (Exception e) {
            // Catch-all for unexpected errors
            showAlert("Error: " + e.getMessage());
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
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null); // We don’t need a title
        a.setContentText(msg); // Show our message
        a.showAndWait(); // Wait until the user closes it
    }
}

