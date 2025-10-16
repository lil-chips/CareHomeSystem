package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.CareHome;
import edu.rmit.cosc1295.carehome.Resident;
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
 * Used by nurses to record medicine administration.
 */

public class AdministerMedicineController {

    @FXML private ChoiceBox<String> residentChoice;
    @FXML private TextField medicineField;
    @FXML private TextField doseField;
    @FXML private TextField timeField;
    @FXML private Button recordBtn;
    @FXML private Button backBtn;

    private CareHome model;
    private Staff loggedInStaff;

    /**
     * Called by Dashboard to share the data model.
     * It also fills the resident dropdown list.
     * @param model The shared CareHome model
     */

    public void setModel(CareHome model) {
        this.model = model;
        residentChoice.getItems().clear();
        for (Resident r : model.getResidents()) {
            residentChoice.getItems().add(r.getName());
        }
        if (!residentChoice.getItems().isEmpty())
            residentChoice.setValue(residentChoice.getItems().get(0));
    }

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    /**
     * When nurse clicks “Record Administration”.
     * @param event Button click event
     */

    @FXML
    void onRecord(ActionEvent event) {
        String residentName = residentChoice.getValue();
        String medicine = medicineField.getText();
        String dose = doseField.getText();
        String time = timeField.getText();

        if (residentName == null || medicine.isBlank() || dose.isBlank() || time.isBlank()) {
            showAlert("Please fill in all fields.");
            return;
        }

        try {
            CareHome.createLog("Nurse " + loggedInStaff.getName() +
                    " administered " + medicine + " (" + dose + ") to " + residentName + " at " + time);
            showAlert("Record saved successfully!");
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
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null); // We don’t need a title
        a.setContentText(msg); // Show our message
        a.showAndWait(); // Wait until the user closes it
    }
}

