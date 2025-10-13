package edu.rmit.cosc1295.ui;

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

    @FXML
    void onLogin(ActionEvent event) {

    }


}
