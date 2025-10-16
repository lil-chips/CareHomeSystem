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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ResidentListController {

    @FXML private TableView<Resident> residentTable;
    @FXML private TableColumn<Resident, String> nameCol;
    @FXML private TableColumn<Resident, String> genderCol;
    @FXML private TableColumn<Resident, Integer> bedCol;
    @FXML private TableColumn<Resident, String> prescriptionCol;
    @FXML private Button backBtn;

    private CareHome model;
    private Staff loggedInStaff;

    /**
     * Receives the shared CareHome model
     * and shows all residents in the table.
     * It fills the table with each resident's name, gender, and bed ID.
     *
     * @param model The CareHome object that stores all data
     */

    public void setModel(CareHome model) {
        this.model = model;

        // If model or list is empty, stop here
        if (model == null || model.getResidents() == null) {
            showAlert("No resident data found.");
            return;
        }

        // Convert list to ObservableList for TableView
        ObservableList<Resident> data = FXCollections.observableArrayList(model.getResidents());
        residentTable.setItems(data);

        // Put the list into the table
        residentTable.setItems(data);

        // Set up each column to show the correct value
        // For the "Name" column, show each resident’s name
        nameCol.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName());
        });

        // For the "Gender" column, show each resident’s gender
        genderCol.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGender());
        });

        // For the "Bed ID" column, show the bed number (which is an Integer)
        bedCol.setCellValueFactory(cellData -> {
            // Use SimpleObjectProperty because bedId is an Integer, not String
            return new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getBedId());
        });

        // Column: Prescriptions summary
        prescriptionCol.setCellValueFactory(c -> {
            StringBuilder sb = new StringBuilder();
            for (Prescription p : c.getValue().getPrescriptions()) {
                sb.append(p.getMedicine()).append(" (").append(p.getDose()).append("), ");
            }
            return new javafx.beans.property.SimpleStringProperty(sb.toString());
        });

        // Double-click on a row to open the details page
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
     * Opens the ViewResidentDetails.fxml for the selected resident.
     */

    private void openResidentDetails(Resident resident, javafx.scene.input.MouseEvent event) {
        try {
            if (resident == null) {
                showAlert("Please select a resident to view.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/ViewResidentDetails.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);

            ViewResidentDetailsController controller = loader.getController();
            controller.setData(model, resident, loggedInStaff);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Resident Details");
            stage.show();
        } catch (Exception e) {
            // Catch-all for unexpected errors
            showAlert("Failed to open resident details: " + e.getMessage());
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
            showAlert("Something went wrong: " + e.getMessage());
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
        alert.showAndWait();       // Wait until the user closes it
    }
}


