package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.CareHome;
import edu.rmit.cosc1295.carehome.Staff;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class AddStaffController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField passwordField;
    @FXML private ChoiceBox<String> roleChoice;

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
}
