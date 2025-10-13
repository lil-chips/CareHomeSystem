package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.CareHome;
import edu.rmit.cosc1295.carehome.Staff;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Label;


public class DashboardController {

    @FXML
    private Label welcomeLabel;
    private CareHome model;
    private String userId;
    private String role;
    private Staff loggedInStaff;

    @FXML
    private Button staffListBtn;

    /**
     * Receive model and user info from LoginController
     */

    public void setData(CareHome model, String userId, String role) {
        this.model = model;
        this.userId = userId;
        this.role = role;
        welcomeLabel.setText("Welcome " + role + " " + userId + "!");
    }

    /**
     * Let the dashboard receive the shared CareHome model
     * Gives the controller access to all system data
     * @param model The CareHome model
     */

    public void setModel(CareHome model) {
        this.model = model;
    }

    /**
     * Passes the logged-in staff member to the dashboard and update the welcome message
     * @param staff Who logged in
     */

    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
        welcomeLabel.setText("Welcome, " + staff.getName() + " (" + staff.getClass().getSimpleName() + ")");
    }

    @FXML
    void onLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/login.fxml"));
            Scene loginScene = new Scene(loader.load(), 600, 400);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("CareHome - Login");
            stage.show();
            System.out.println("Logout success!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onOpenStaffList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/staff_list.fxml"));
            Scene staffScene = new Scene(loader.load(), 600, 400);

            // Pass the model and user info to the staff list controller
            StaffListController controller = loader.getController();
            controller.setData(model, userId, role);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(staffScene);
            stage.setTitle("CareHome - Staff List");
            stage.show();

            System.out.println("Opened Staff List screen!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add resident button
     * @param event to add a new resident
     */

    @FXML
    void onAddResident(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/add_resident.fxml"));
            Scene scene = new Scene(loader.load(), 500, 350);

            AddResidentController controller = loader.getController();
            controller.setModel(model);
            controller.setLoggedInStaff(loggedInStaff);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Add Resident");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
