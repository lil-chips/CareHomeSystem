package edu.rmit.cosc1295.ui;

import javafx.fxml.FXML;
import edu.rmit.cosc1295.carehome.CareHome;
import edu.rmit.cosc1295.carehome.Staff;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

/**
 * Displays all staff in a table view
 */

public class StaffListController {

    @FXML
    private TableView<Staff> staffTable;

    @FXML
    private TableColumn<Staff, String> idCol;

    @FXML
    private TableColumn<Staff, String> nameCol;

    @FXML
    private TableColumn<Staff, String> roleCol;

    private CareHome model;

    /**
     *
     * @param model
     */

    public void setModel(CareHome model) {
        this.model = model;

    }
}
