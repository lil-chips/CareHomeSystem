package edu.rmit.cosc1295.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import edu.rmit.cosc1295.carehome.CareHome;
import edu.rmit.cosc1295.carehome.Staff;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Displays all staff in a table view
 */

public class StaffListController {

    @FXML
    private TableView<Staff> staffTable;

    @FXML
    private TableColumn<Staff, String> idCol;

    @FXML
    private TableColumn<Staff, String> nameCol;

    @FXML
    private TableColumn<Staff, String> roleCol;

    @FXML
    private Button backBtn;

    @FXML
    private Label titleLabel;

    private CareHome model;
    private String userId;
    private String role;

    /**
     * Called from DashboardController to reuse the same CareHome data here.
     * @param model the CareHome object we got from the main app
     */

    public void setModel(CareHome model) {
        this.model = model;

    }

    /**
     * Get data from the previous scene (Dashboard)
     * Used to access the same CareHome object and know who logged in
     */

    public void setData(CareHome model, String userId, String role) {
        this.model = model;
        this.userId = userId;
        this.role = role;

        // Just a display on top
        titleLabel.setText("Staff List - Logged in as " + role + " (" + userId + ")");
    }

    /**
     * Load all staff into the table
     */
    private void loadStaffData() {
        ArrayList<Staff> list = model.getStaffList(); // already exists in your CareHome class
        ObservableList<Staff> observableList = FXCollections.observableArrayList(list);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Show each staff member's role
        roleCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getClass().getSimpleName()
                )
        );

        staffTable.setItems(observableList);
    }

    /**
     * Go back to the main Dashboard screen
     * @param event the button click event
     */

    @FXML
    void onBack(ActionEvent event) {
        try {
            // Load the FXML file for the dashboard screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/dashboard.fxml"));

            // Create a new Scene using that FXML file (set width and height)
            Scene dashboardScene = new Scene(loader.load(), 600, 400);

            // Get the controller class (DashboardController) linked to dashboard.fxml
            DashboardController controller = loader.getController();

            // Get the current window (stage) from the button event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Switch the screen from login to dashboard
            stage.setScene(dashboardScene);

            // Set the window title
            stage.setTitle("CareHome - Dashboard");

            // Show the new window (dashboard)
            stage.show();

        } catch (Exception e) {
            //print the error details in the console
            showAlert("Failed to return to dashboard: " + e.getMessage());
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
