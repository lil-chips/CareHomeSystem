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
import javafx.stage.Stage;

public class AssignShiftController {
    @FXML private ChoiceBox<String> staffChoice;
    @FXML private ChoiceBox<String> dayChoice;
    @FXML private ChoiceBox<String> timeChoice;
    @FXML private Button assignBtn;
    @FXML private Button backBtn;

    private CareHome model;
    private Staff loggedInStaff; // should be Manager

    /**
     * Called automatically when the page loads.
     * Prepare dropdown lists for day and time.
     */

    @FXML
    public void initialize() {
        dayChoice.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

        // Time list will depend on the selected staff type (Doctor/Nurse)
        timeChoice.getItems().clear();
    }

    /**
     * Called by DashboardController to inject shared data.
     * @param model The CareHome data model
     */

    public void setModel(CareHome model) {
        this.model = model;

        // Fill staff list (Doctor + Nurse only)
        staffChoice.getItems().clear();
        for (Staff s : model.getStaffList()) {
            if (s instanceof Doctor || s instanceof Nurse) {
                staffChoice.getItems().add(s.getId() + " - " + s.getName());
            }
        }
        // If we have staff, pre-select the first one
        if (!staffChoice.getItems().isEmpty()) {
            staffChoice.setValue(staffChoice.getItems().getFirst());
        }

        // When user selects a staff, show the correct time choices
        staffChoice.setOnAction(e -> updateTimeChoices());
    }

    /**
     * When staff selection changes, update time options.
     */

    private void updateTimeChoices() {
        timeChoice.getItems().clear(); // clear previous options

        if (staffChoice.getValue() == null) return;

        // Get staff ID from "d1 - Alice"
        String staffId = staffChoice.getValue().split(" - ")[0];
        Staff selected = model.findStaffById(staffId);

        if (selected == null) return;

        // Doctor can only work 1hr
        if (selected instanceof Doctor) {
            timeChoice.getItems().add("1hr");
            timeChoice.setValue("1hr");
        }
        // Nurse can work Morning or Afternoon
        else if (selected instanceof Nurse) {
            timeChoice.getItems().addAll("Morning", "Afternoon");
            timeChoice.setValue("Morning");
        }
    }

    /**
     * Called by DashboardController to know which Manager is logged in.
     * @param staff The logged-in Manager
     */

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    /**
     * Handle the Assign Shift button click.
     * @param event The button click event
     */

    @FXML
    void onAssignShift(ActionEvent event) {
        try {
            String staffInfo = staffChoice.getValue();
            String day = dayChoice.getValue();
            String time = timeChoice.getValue();

            if (staffInfo == null || day == null || time == null) {
                showAlert("Please select all fields before assigning.");
                return;
            }
            // Get staff ID from string like "n1 - Lucy"
            String staffId = staffInfo.split(" - ")[0];
            Staff selected = model.findStaffById(staffId);

            if (selected == null) {
                showAlert("Staff not found!");
                return;
            }

            Shift shift = new Shift(day, time);
            // Assign the shift
            model.assignShift((Manager) loggedInStaff, selected, shift);

            showAlert("Shift assigned successfully!");
            onBack(event);
        } catch (Exception e) {
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null); // We don’t need a title
        alert.setContentText(msg); // Show our message
        alert.showAndWait(); // Wait until the user closes it
    }
}
