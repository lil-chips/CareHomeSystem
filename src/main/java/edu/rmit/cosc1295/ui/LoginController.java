package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.CareHome;
import edu.rmit.cosc1295.carehome.Staff;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Controller for login.fxml, handles login UI actions.
 */
public class LoginController {

    @FXML
    private TextField idField;

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox<String> roleChoice;

    // Reference to the careHome model, shared data for the whole system
    private CareHome model;

    @FXML
    public void initialize() {
        // Initialize the role choices
        roleChoice.getItems().addAll("Manager", "Doctor", "Nurse");
        roleChoice.setValue("Manager"); // default value
    }

    @FXML
    void onLogin(ActionEvent event) {

        // Show alert if model is null instead of NullPointer
        if (model == null) {
            showAlert("System model not loaded. Please restart the app.");
            return;
        }

        String id = idField.getText().trim();
        String pass = passwordField.getText().trim();
        String role = roleChoice.getValue();

        if (id.isEmpty() || pass.isEmpty()) {
            showAlert("Please enter ID and password!");
            return;
        }

        // Check the account, does it exist
        Staff found = null;
        for (Staff s : model.getStaffList()) {
            if (s.getId().equals(id) && s.getPassword().equals(pass)) {
                found = s;
                break;
            }
        }

        // Can't find the account
        if (found == null) {
            showAlert("Invalid ID or password.");
            return;
        }

        // Check the role
        if (!found.getClass().getSimpleName().equalsIgnoreCase(role)) {
            showAlert("This ID does not belong to role: " + role);
            return;
        }

        try {
            // If login info looks fine, load dashboard.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/dashboard.fxml"));

            // Create a new Scene using that FXML file (set width and height)
            Scene dashboardScene = new Scene(loader.load(), 600, 400);

            // Get controller & inject data
            DashboardController dashboardController = loader.getController();
            dashboardController.setModel(model);
            dashboardController.setLoggedInStaff(found);

            // Replace the current scene with the new one
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setTitle("CareHome - Dashboard");
            stage.show();

            System.out.println("Login success! Jumped to dashboard.");

        } catch (Exception e){
            e.printStackTrace();
            showAlert("Failed to load dashboard: " + e.getMessage());
        }

        System.out.println("Login success :) Role: " + role + ", ID: " + id);
        showAlert("Login success!\nRole: " + role + "\nID: " + id);
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    /**
     * Let MainApp inject the CareHome model into this controller
     * @param model The CareHome model
     */
    public void setModel(CareHome model) {
        this.model = model;
    }
}
