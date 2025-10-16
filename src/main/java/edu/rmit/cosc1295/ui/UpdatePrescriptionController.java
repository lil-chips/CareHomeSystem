package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Doctor selects a bed, views all prescriptions,
 * clicks one to edit its fields.
 */

public class UpdatePrescriptionController {
    @FXML private ChoiceBox<Integer> bedChoice;
    @FXML private TableView<Prescription> presTable;
    @FXML private TableColumn<Prescription, String> doctorCol;
    @FXML private TableColumn<Prescription, String> medicineCol;
    @FXML private TableColumn<Prescription, String> doseCol;
    @FXML private TableColumn<Prescription, String> timeCol;

    @FXML private TextField medicineField;
    @FXML private TextField doseField;
    @FXML private TextField timeField;
    @FXML private Button updateBtn;
    @FXML private Button backBtn;
    @FXML private Button loadBtn;

    private CareHome model;
    private Staff loggedInStaff;
    private Resident selectedResident;
    private int selectedIndex = -1;

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

        // When the user clicks on a row in the table,
        // display the data of that prescription in the text fields below
        presTable.setOnMouseClicked(event -> {
            // Check if a row is actually selected
            if (presTable.getSelectionModel().getSelectedItem() != null) {
                // Get the selected prescription
                Prescription selectedPrescription = presTable.getSelectionModel().getSelectedItem();

                // Save which row index is currently selected
                selectedIndex = presTable.getSelectionModel().getSelectedIndex();

                // Show the existing values in the text fields
                medicineField.setText(selectedPrescription.getMedicine());
                doseField.setText(selectedPrescription.getDose());
                timeField.setText(selectedPrescription.getTime());
            }
        });
    }

    /**
     * Called from DashboardController to reuse the same CareHome data here.
     * @param model the CareHome object we got from the main app
     */

    public void setModel(CareHome model) {
        this.model = model;
        for (Bed b : model.getBeds()) {
            if (!b.bedAvailable()) bedChoice.getItems().add(b.getBedId());
        }
        if (!bedChoice.getItems().isEmpty())
            bedChoice.setValue(bedChoice.getItems().getFirst());
    }

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    /**
     * Handles the "Load Prescriptions" button click event.
     * @param event the button click event
     */

    @FXML
    void onLoadPrescriptions(ActionEvent event) {

        // Get the selected bed ID from the dropdown list
        Integer bedId = bedChoice.getValue();

        // Check if the user actually selected a bed
        if (bedId == null) {
            showAlert("Please select a bed first.");
            return;
        }

        // Find the bed in the model
        Bed bed = model.findBedById(bedId);

        // Validate that the bed exists and has a resident assigned
        if (bed == null || bed.getResident() == null) {
            showAlert("No resident found in this bed.");
            return;
        }

        // Save the resident for later use
        selectedResident = bed.getResident();

        // Get all prescriptions belonging to this resident
        ObservableList<Prescription> data = FXCollections.observableArrayList(selectedResident.getPrescriptions());

        // Display the prescriptions in the table
        presTable.setItems(data);
    }

    /**
     * Handles the "Update" button click event on the Update Prescription screen.
     * @param event the button click event
     */

    @FXML
    void onUpdatePrescription(ActionEvent event) {
        try {
            // Only doctors are allowed to perform this action
            if (!(loggedInStaff instanceof Doctor)) {
                showAlert("Only doctors can update prescriptions!");
                return;
            }

            // Make sure a resident and a prescription have been selected
            if (selectedResident == null || selectedIndex < 0) {
                showAlert("Please select a prescription to update.");
                return;
            }

            // Read and clean input values from text fields
            String newMed = medicineField.getText().trim();
            String newDose = doseField.getText().trim();
            String newTime = timeField.getText().trim();

            // Ensure all fields are filled before updating
            if (newMed.isEmpty() || newDose.isEmpty() || newTime.isEmpty()) {
                showAlert("All fields must be filled.");
                return;
            }

            // Call the backend function in CareHome to update the prescription
            model.docUpdatePres(
                    (Doctor) loggedInStaff,
                    selectedResident.getBedId(),
                    selectedIndex,
                    newMed, newDose, newTime
            );

            // Refresh the table to show the updated data
            presTable.setItems(FXCollections.observableArrayList(
                    selectedResident.getPrescriptions()
            ));

            // Show a confirmation message
            showAlert("Prescription updated successfully!");

        } catch (Exception e) {
            // Display any unexpected errors that occur during the process
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


