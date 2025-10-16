package edu.rmit.cosc1295.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import edu.rmit.cosc1295.carehome.CareHome;
import edu.rmit.cosc1295.carehome.Staff;
import javafx.scene.Parent;
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
    private Staff loggedInStaff;

    /**
     * Called from DashboardController to reuse the same CareHome data here.
     * @param model the CareHome object we got from the main app
     */

    public void setModel(CareHome model) {
        this.model = model;

    }

    /**
     * Save info about who is logged in.
     * @param staff The logged-in nurse
     */

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    /**
     * Get data from the previous scene (Dashboard)
     * Used to access the same CareHome object and know who logged in
     */

    public void setData(CareHome model, String userId, String role) {
        // Store the references for later use in this controller
        this.model = model;
        this.userId = userId;
        this.role = role;

        // If the title label exists, show the current user's role and ID
        if (titleLabel != null) {
            titleLabel.setText("Staff List - Logged in as " + role + " (" + userId + ")");
        }

        // Make sure table shows data if we came through this path
        loadStaffData();
    }

    /**
     * Loads all staff data from the shared CareHome model
     */

    private void loadStaffData() {
        // Ensure both the model and table exist
        if (model == null || staffTable == null) return;

        // Retrieve the staff list from the CareHome model
        ArrayList<Staff> list = model.getStaffList(); // already exists in your CareHome class

        // Convert ArrayList into ObservableList for JavaFX binding
        ObservableList<Staff> observableList = FXCollections.observableArrayList(list);

        // Map table columns to Staff attributes using PropertyValueFactory
        // These property names must match getter methods in Staff class (getId(), getName())
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Dynamically display each staff member's role (class name)
        roleCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getClass().getSimpleName()
                )
        );

        // Bind the populated data to the TableView
        staffTable.setItems(observableList);
    }

    /**
     * Called when the user clicks the "Back" button.
     * This method switches the current scene back to the dashboard.
     * @param event The button click event used to get the current window
     */

    @FXML
    void onBack(ActionEvent event) {
        try {
            // Load the FXML file for the dashboard screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/dashboard.fxml"));
            Parent root = loader.load();

            // Get the controller class (DashboardController) linked to dashboard.fxml
            DashboardController controller = loader.getController();
            controller.setModel(model);
            controller.setLoggedInStaff(loggedInStaff);

            // Create a new Scene using that FXML file (set width and height)
            Scene dashboardScene = new Scene(root, 600, 400);

            // Get the current window (stage) from the button event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Switch the screen from login to dashboard
            stage.setScene(dashboardScene);

            // Set the window title
            stage.setTitle("CareHome - Dashboard");

            // Show the new window (dashboard)
            stage.show();

        } catch (Exception e) {
            // Handle any loading or transition errors
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
