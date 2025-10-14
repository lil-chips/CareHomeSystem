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

    public void setModel(CareHome model) {
        this.model = model;
    }

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    /**
     * Called when the user clicks "Add Bed"
     */

    @FXML
    void onAddBed(ActionEvent event) {
        String bedIdText = bedIdField.getText().trim();

        if (bedIdText.isEmpty()) {
            showAlert("Please enter a Bed ID!");
            return;
        }

        try {
            int bedId = Integer.parseInt(bedIdText);
            Bed newBed = new Bed(bedId);
            model.addBed(newBed);
            CareHome.createLog(loggedInStaff.getName() + " added new bed " + bedId);

            showAlert("Bed added successfully!");
            onBack(event);

        } catch (NumberFormatException e) {
            showAlert("Bed ID must be a number!");
        } catch (Exception e) {
            showAlert("Failed to add bed: " + e.getMessage());
        }
    }

    /**
     * Go back to dashboard
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

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

    }
}
