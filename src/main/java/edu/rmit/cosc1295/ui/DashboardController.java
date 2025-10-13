package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.CareHome;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DashboardController {

    @FXML
    private Label welcomeLabel;
    private CareHome model;
    private String userId;
    private String role;

    /**
     * Receive model and user info from LoginController
     */

    public void setData(CareHome model, String userId, String role) {
        this.model = model;
        this.userId = userId;
        this.role = role;
        welcomeLabel.setText("Welcome " + role + " " + userId + "!");
    }

    @FXML
    void onLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/login.fxml"));
            Scene loginScene = new Scene(loader.load(), 600, 400);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        } catch (Exception e) {

        }

    }
}
