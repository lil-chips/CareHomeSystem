package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.CareHome;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

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
    void onLogin(ActionEvent event) {
        String id = idField.getText();
        String pass = passwordField.getText();
        String role = roleChoice.getValue();

        if (id == null || id.isBlank() || pass == null || pass.isBlank()) {
            showAlert("Please enter ID and password!");
            return;
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
