package edu.rmit.cosc1295.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.awt.*;

/**
 * Controller for MoveResident.fxml
 * Allows nurses to move a resident from one bed to another.
 */

public class MoveResidentController {

    @FXML
    private ChoiceBox<String> residentChoice;

    @FXML
    private ChoiceBox<Integer> bedChoice;

    private CareHome model;
    private Staff loggedInStaff;

}
