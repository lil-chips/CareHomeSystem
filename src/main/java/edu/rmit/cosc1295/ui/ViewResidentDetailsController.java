package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.CareHome;
import edu.rmit.cosc1295.carehome.Prescription;
import edu.rmit.cosc1295.carehome.Resident;
import edu.rmit.cosc1295.carehome.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Displays a table of all residents and their details.
 */

public class ViewResidentDetailsController {

    @FXML private Label nameLabel;
    @FXML private Label genderLabel;
    @FXML private Label bedLabel;

    // This table shows all prescriptions
    @FXML private TableView<Prescription> prescriptionTable;
    @FXML private TableColumn<Prescription, String> doctorCol;
    @FXML private TableColumn<Prescription, String> medicineCol;
    @FXML private TableColumn<Prescription, String> doseCol;
    @FXML private TableColumn<Prescription, String> timeCol;

    @FXML private Button backBtn;

    private CareHome model;
    private Resident selectedResident;
    private Staff loggedInStaff;

    /**
     * Initialize the table columns.
     * This runs automatically when the FXML is loaded.
     */

    @FXML
    public void initialize() {

        // Doctor ID column show doctor
        doctorCol.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("doctorId")
        );

        // Medicine column show medicine name
        medicineCol.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("medicine")
        );

        // Dose column show dosage
        doseCol.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("dose")
        );

        // Time column show time
        timeCol.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("time")
        );
    }

    public void setData(CareHome model, Resident resident, Staff staff) {
        this.model = model;
        this.selectedResident = resident;
        this.loggedInStaff = staff;

        if (resident == null) {
            showAlert("Resident not found!");
            return;
        }

        // Display resident basic info
        nameLabel.setText(resident.getName());
        genderLabel.setText(resident.getGender());

        // If not assign to a bed yet show "Not assigned"
        if (resident.getBedId() != null) {
            bedLabel.setText(String.valueOf(resident.getBedId()));
        } else {
            bedLabel.setText("Not assigned");
        }

        // Get the resident's prescriptions
        ArrayList<Prescription> list = resident.getPrescriptions();

        // Convert to a format that TableView understands
        ObservableList<Prescription> data = FXCollections.observableArrayList(list);

        // Put the data inside the table
        prescriptionTable.setItems(data);

        if (list.isEmpty()) {
            showAlert("This resident has no prescriptions yet!");
        }
    }


    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
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


