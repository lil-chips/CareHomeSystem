package edu.rmit.cosc1295.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import edu.rmit.cosc1295.carehome.CareHome;
import edu.rmit.cosc1295.carehome.Staff;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
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

    private CareHome model;

    /**
     * Called from DashboardController to reuse the same CareHome data here.
     * @param model the CareHome object we got from the main app
     */

    public void setModel(CareHome model) {
        this.model = model;

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
            e.printStackTrace();
        }
    }
}
