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

    @FXML private TableView<Resident> residentTable;
    @FXML private TableColumn<Resident, String> nameCol;
    @FXML private TableColumn<Resident, String> genderCol;
    @FXML private TableColumn<Resident, Integer> bedCol;
    @FXML private TableColumn<Resident, String> prescriptionCol;
    @FXML private Button backBtn;

    @FXML private Label nameLabel;
    @FXML private Label genderLabel;
    @FXML private Label bedLabel;

    // This table shows all prescriptions
    @FXML private TableView<Prescription> prescriptionTable;
    @FXML private TableColumn<Prescription, String> doctorCol;
    @FXML private TableColumn<Prescription, String> medicineCol;
    @FXML private TableColumn<Prescription, String> doseCol;
    @FXML private TableColumn<Prescription, String> timeCol;

    private CareHome model;
    private Resident selectedResident;
    private Staff loggedInStaff;


    public void setData(CareHome model, Resident resident, Staff staff) {
        this.model = model;
        this.selectedResident = resident;
        this.loggedInStaff = staff;

        // Display resident basic info
        nameLabel.setText(resident.getName());
        genderLabel.setText(resident.getGender());
        bedLabel.setText(String.valueOf(resident.getBedId()));

        // Get the resident's prescriptions
        ArrayList<Prescription> list = resident.getPrescriptions();

        // Convert to a format that TableView understands
        ObservableList<Prescription> data = FXCollections.observableArrayList(list);

        // Tell TableView which column shows which data
        doctorCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getDoctorId()));

        medicineCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getMedicine()));

        doseCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getDose()));

        timeCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getTime()));

        // Put the data into the table
        prescriptionTable.setItems(data);
    }

    /**
     * Get the shared CareHome data and show all residents in the table.
     * @param model The CareHome object that stores all residents
     */

    public void setModel(CareHome model) {
        this.model = model;

        // Load resident data into the table
        // Convert the resident list from CareHome into an ObservableList
        ObservableList<Resident> data = FXCollections.observableArrayList(model.getResidents());

        // Put this data into our table
        residentTable.setItems(data);

        // For the "Name" column, show each resident’s name
        nameCol.setCellValueFactory(cellData -> {
            String name = cellData.getValue().getName(); // get the name from Resident
            return new javafx.beans.property.SimpleStringProperty(name);
        });

        // For the "Gender" column, show each resident’s gender
        genderCol.setCellValueFactory(cellData -> {
            String gender = cellData.getValue().getGender();
            return new javafx.beans.property.SimpleStringProperty(gender);
        });

        // For the "Bed ID" column, show the bed number (which is an Integer)
        bedCol.setCellValueFactory(cellData -> {
            Integer bedId = cellData.getValue().getBedId();
            return new javafx.beans.property.SimpleObjectProperty<>(bedId);
        });

        // Combine all prescriptions into one string
        // If a resident has multiple prescriptions, we put them together in one line.
        prescriptionCol.setCellValueFactory(cellData -> {
            Resident resident = cellData.getValue(); // get this resident
            StringBuilder sb = new StringBuilder();  // used to build a long string

            // Go through all prescriptions for this resident
            for (Prescription p : resident.getPrescriptions()) {
                sb.append(p.getMedicine()).append(" (").append(p.getDose()).append("), ");
            }

            // Convert the string into a form that TableView can display
            return new javafx.beans.property.SimpleStringProperty(sb.toString());
        });

        // Add listener to open detail view when a resident is double-clicked
        residentTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !residentTable.getSelectionModel().isEmpty()) {
                Resident selected = residentTable.getSelectionModel().getSelectedItem();
                openResidentDetails(selected, event);
            }
        });
    }

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    /**
     * Double-click event → opens ViewResidentDetails screen
     */

    private void openResidentDetails(Resident resident, javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/ViewResidentDetails.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);

            ViewResidentDetailsController controller = loader.getController();
            controller.setData(model, resident, loggedInStaff);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Resident Details");
            stage.show();

        } catch (Exception e) {
            showAlert("Failed to open details: " + e.getMessage());
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


