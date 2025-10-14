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

/**
 * Displays a table of all residents and their details.
 */

public class ViewResidentsController {

    @FXML private TableView<Resident> residentTable;
    @FXML private TableColumn<Resident, String> nameCol;
    @FXML private TableColumn<Resident, String> genderCol;
    @FXML private TableColumn<Resident, Integer> bedCol;
    @FXML private TableColumn<Resident, String> prescriptionCol;
    @FXML private Button backBtn;

    private CareHome model;
    private Staff loggedInStaff;

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
                sb.append(p.getMedicine())
                        .append(" (")
                        .append(p.getDose())
                        .append("), ");
            }

            // Convert the string into a form that TableView can display
            return new javafx.beans.property.SimpleStringProperty(sb.toString());
        });
    }

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
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


