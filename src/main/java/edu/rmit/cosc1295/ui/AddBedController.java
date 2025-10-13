package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller for AddBed.fxml
 * Allows manager to add a new bed into the system.
 */

public class AddBedController {

    @FXML
    private TextField bedIdField;

    private CareHome model;
    private Staff loggedInStaff;

    public void setModel(CareHome model) {
        this.model = model;
    }

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }


}
