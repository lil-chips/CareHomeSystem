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

        // Open a FileChooser dialog for the user to select export location
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export System Logs");

        // Restrict selectable file type to .txt only
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Text Files", "*.txt"));

        // Suggest a default file name using the current date
        chooser.setInitialFileName("care_home_logs_" + java.time.LocalDate.now() + ".txt");

        // Show save dialog and get the selected file
        File file = chooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

        // If the user selected a file, proceed with export
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {

                // Write file header
                writer.write("~~~~ CareHome System Log Export ~~~~\n");
                writer.write("Exported at: " + java.time.LocalDateTime.now() + "\n");
                writer.write("----------------------------------------\n\n");

                // Write each log entry to the file, line by line
                for (String log : logs) {
                    writer.write(log + System.lineSeparator());
                }

                // Notify user that export succeeded
                showAlert("Logs exported successfully to:\n" + file.getAbsolutePath());
            } catch (IOException e) {
                // Handle any I/O errors that occur during writing
                showAlert("Failed to export logs:\n" + e.getMessage());
            }
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
     * Helper function to show pop-up messages.
     * @param msg The message text
     */

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null); // We don’t need a title
        a.setContentText(msg); // Show our message
        a.showAndWait(); // Wait until the user closes it
    }
}

