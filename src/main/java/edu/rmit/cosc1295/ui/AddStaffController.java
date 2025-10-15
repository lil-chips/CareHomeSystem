package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddStaffController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField passwordField;
    @FXML private ChoiceBox<String> roleChoice;
    @FXML private Button addBtn;
    @FXML private Button backBtn;

    private CareHome model;
    private Staff loggedInStaff;

    @FXML
    public void initialize() {
        // Provide role options
        roleChoice.getItems().addAll("Manager", "Doctor", "Nurse");
        roleChoice.setValue("Nurse");
    }

    public void setModel(CareHome model) {
        this.model = model;
    }

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    @FXML
    void onAddStaff(ActionEvent event) {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleChoice.getValue();

        if (id.isEmpty() || name.isEmpty() || password.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        try {
            Staff newStaff;
            switch (role) {
                case "Manager" -> newStaff = new Manager(id, name, password);
                case "Doctor" -> newStaff = new Doctor(id, name, password);
                case "Nurse"  -> newStaff = new Nurse(id, name, password);
                default -> throw new IllegalArgumentException("Unknown role: " + role);
            }

            model.addStaff((Manager) loggedInStaff, newStaff);
            CareHome.createLog("Manager " + loggedInStaff.getName() + " added new " + role + ": " + name);

            showAlert("Staff added successfully!");
            onBack(event);

        } catch (Exception e) {
            showAlert("Failed to add staff: " + e.getMessage());
        }
    }

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

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

