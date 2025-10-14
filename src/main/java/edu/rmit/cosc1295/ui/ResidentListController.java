package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.CareHome;
import edu.rmit.cosc1295.carehome.Resident;
import edu.rmit.cosc1295.carehome.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

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

    /**
     * Receives the shared CareHome model
     * and shows all residents in the table.
     * It fills the table with each resident's name, gender, and bed ID.
     *
     * @param model The CareHome object that stores all data
     */

    public void setModel(CareHome model) {
        this.model = model;

        // Create a list that JavaFX can use in the table
        ObservableList<Resident> data = FXCollections.observableArrayList();

        // Loop through every resident and add them to the list
        for (Resident r : model.getResidents()) {
            data.add(r);
        }

        // Put the list into the table
        residentTable.setItems(data);

        // Set up each column to show the correct value
        // This means: the name column shows r.getName()
        nameCol.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName());
        });

        genderCol.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGender());
        });

        bedCol.setCellValueFactory(cellData -> {
            // Use SimpleObjectProperty because bedId is an Integer, not String
            return new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getBedId());
        });
    }



    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
    }

    /**
     * Return to dashboard when user clicks "Back".
     * @param event The button click event
     */

    @FXML
    void onBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/Dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);

            DashboardController controller = loader.getController();
            controller.setModel(model);
            controller.setLoggedInStaff(loggedInStaff);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Dashboard");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


