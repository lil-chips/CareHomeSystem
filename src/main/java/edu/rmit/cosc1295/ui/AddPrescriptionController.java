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
 * Controller for AddPrescription.fxml
 * Lets doctors add a new prescription to an existing resident.
 */

public class AddPrescriptionController {

    @FXML
    private ChoiceBox<String> residentChoice;

    @FXML
    private TextField medicineField;

    @FXML
    private TextField doseField;

    @FXML
    private TextField timeField;

    private CareHome model;
    private Staff loggedInStaff;

    public void setModel(CareHome model) {
        this.model = model;

        // Populate resident names
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

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
