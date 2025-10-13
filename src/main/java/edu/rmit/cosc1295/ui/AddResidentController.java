package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.*;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.awt.*;

public class AddResidentController {
    @FXML
    private TextField nameField;

    @FXML
    private ChoiceBox<String> genderChoice;

    @FXML
    private ChoiceBox<Integer> bedChoice;

    private CareHome model;
    private Staff loggedInStaff;

    @FXML
    public void initialize() {
        // Fill gender options
        genderChoice.getItems().addAll("Male", "Female", "Other");
        genderChoice.setValue("Male");
    }
}
