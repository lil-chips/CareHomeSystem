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

    /**
     * Let the dashboard receive the shared CareHome model
     * Gives the controller access to all system data
     * @param model The CareHome model
     */

    public void setModel(CareHome model) {
        this.model = model;

        // Populate empty bed IDs
        bedChoice.getItems().clear();
        for (Bed b : model.getBeds()) {
            // Only show beds that are still available (not occupied)
            if (b.bedAvailable()) {
                bedChoice.getItems().add(b.getBedId()); // Add the bed ID to the dropdown list
            }
        }
        // If there are any available beds, automatically select the first one
        if (!bedChoice.getItems().isEmpty())
            bedChoice.setValue(bedChoice.getItems().get(0));
    }




}
