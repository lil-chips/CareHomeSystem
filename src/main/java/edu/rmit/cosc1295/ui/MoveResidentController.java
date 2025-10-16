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
        this.model = model;

        // Fill resident names
        residentChoice.getItems().clear();
        for (Resident r : model.getResidents()) {
            residentChoice.getItems().add(r.getName());
        }

        // Fill only empty beds
        bedChoice.getItems().clear();
        for (Bed b : model.getBeds()) {
            if (b.bedAvailable()) {
                bedChoice.getItems().add(b.getBedId());
            }
        }

        // Default selections
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
        if (!(loggedInStaff instanceof Nurse)) {
            showAlert("Only nurses can move residents.");
            return;
        }

        String residentName = residentChoice.getValue();
        Integer newBedId = bedChoice.getValue();

        if (residentName == null || newBedId == null) {
            showAlert("Please select both a resident and a new bed.");
            return;
        }

        try {
            model.moveResident((Nurse) loggedInStaff, residentName, newBedId);
            CareHome.createLog("Nurse " + loggedInStaff.getName() +
                    " moved " + residentName + " to bed " + newBedId);

            showAlert("Resident moved successfully!");
            onBack(event);

        } catch (NotWorkingException e) {
            showAlert("!!! " + e.getMessage());
        } catch (Exception e) {
            showAlert("Failed to move resident: " + e.getMessage());
        }
    }

    /**
     * Go back to the dashboard screen.
     * @param event The button click event
     */

    @FXML
    void onBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);

            DashboardController controller = loader.getController();
            controller.setModel(model);
            controller.setLoggedInStaff(loggedInStaff);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Dashboard");
            stage.show();

        } catch (Exception e) {
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
