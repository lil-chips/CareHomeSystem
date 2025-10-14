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
    }

    /**
     * Go back to the dashboard screen.
     * @param event The button click event
     */

    @FXML
    void onBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/Dashboard.fxml"));
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
     * Helper function to show pop-up messages.
     * @param msg The message text
     */

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}

