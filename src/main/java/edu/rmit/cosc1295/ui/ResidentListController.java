package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.CareHome;
import edu.rmit.cosc1295.carehome.Resident;
import edu.rmit.cosc1295.carehome.Staff;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ResidentListController {
    @FXML
    private TableView<Resident> residentTable;

    @FXML
    private TableColumn<Resident, String> nameCol;

    @FXML
    private TableColumn<Resident, String> genderCol;

    @FXML
    private TableColumn<Resident, Integer> bedCol;

    private CareHome model;
    private Staff loggedInStaff;

}
