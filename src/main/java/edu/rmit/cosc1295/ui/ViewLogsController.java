package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.CareHome;
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
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Shows all log entries stored in the CareHome system.
 */

public class ViewLogsController {

    @FXML private ListView<String> logList;
    @FXML private Button refreshBtn;
    @FXML private Button backBtn;
    @FXML private Button exportBtn;

    private CareHome model;
    private Staff loggedInStaff;

    /**
     * Get shared model and load logs
     */

    public void setModel(CareHome model) {
        this.model = model;
        refreshLogs();
    }

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    /**
     * Refresh the log list on screen
     */

    @FXML
    void onRefresh(ActionEvent event) {
        refreshLogs();
    }

    private void refreshLogs() {
        ObservableList<String> logs = FXCollections.observableArrayList(CareHome.getLogs());
        logList.setItems(logs);
    }

    /**
     * Handles the export of all system logs to a text file.
     * @param event the button click event that triggers this method
     */

    @FXML
    void onExport(ActionEvent event) {
        // Retrieve all logs currently stored in the system
        ArrayList<String> logs = CareHome.getLogs();

        // If there are no logs, inform the user and return early
        if (logs.isEmpty()) {
            showAlert("No logs to export.");
            return;
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

