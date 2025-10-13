package edu.rmit.cosc1295.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DashboardController {

    @FXML
    private Button logoutBtn;

    @FXML
    void onLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("You have logged out successfully.");
        alert.showAndWait();

        // Close this window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
