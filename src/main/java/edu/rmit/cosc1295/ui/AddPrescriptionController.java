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

    /**
     * Let the dashboard receive the shared CareHome model
     * Gives the controller access to all system data
     * @param model The shared CareHome object that holds all data
     */

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

    /**
     * Called from Dashboard to know who is logged in
     * Mainly for recording actions in the log
     * @param staff The staff member who is logged in
     */

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    /**
     * Called when the user clicks "Add Prescription
     */

    @FXML
    void onAddPrescription(ActionEvent event) {
        String residentName = residentChoice.getValue();
        String medicine = medicineField.getText().trim();
        String dose = doseField.getText().trim();
        String time = timeField.getText().trim();

        if (residentName == null || medicine.isEmpty() || dose.isEmpty() || time.isEmpty()) {
            showAlert("Please fill all fields.");
            return;
        }

        try {
            model.addPrescription((Doctor) loggedInStaff, residentName, medicine, dose, time);

            // Log + show in console
            CareHome.createLog("Doctor " + loggedInStaff.getName() +
                    " prescribed " + medicine + " (" + dose + ", " + time + ") to " + residentName);
            System.out.println("Prescription added for " + residentName);

            showAlert("Prescription added successfully!");
            onBack(event);

        } catch (UnauthorizedException e) {
            showAlert("Only doctor can add prescriptions!");
        }catch (Exception e) {
            showAlert("Failed to add prescription: " + e.getMessage());
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
