package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.CareHome;
import edu.rmit.cosc1295.carehome.Staff;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    @FXML private Button staffListBtn;
    @FXML private Button residentBtn;
    @FXML private Button addPrescriptionBtn;
    @FXML private Button addResidentBtn;
    @FXML private Button viewLogsBtn;
    @FXML private Button addBedBtn;
    @FXML private Button moveResidentBtn;
    @FXML private Button logoutBtn;
    @FXML private Button assignShiftBtn;
    @FXML private Button administerMedBtn;
    @FXML private Button modifyPasswordBtn;
    @FXML private Button viewResidentsBtn;

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
     * @param model The shared CareHome object that holds all data
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
            showAlert("Failed to return: " + e.getMessage());
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
            showAlert("Failed to return: " + e.getMessage());
        }
    }

    /**
     * Add resident button
     * @param event to add a new resident
     */

    @FXML
    void onAddResident(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/AddResident.fxml"));
            Scene scene = new Scene(loader.load(), 500, 350);

            AddResidentController controller = loader.getController();
            controller.setModel(model);
            controller.setLoggedInStaff(loggedInStaff);

            // Switch the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Add Resident");
            stage.show();
        } catch (Exception e) {
            showAlert("Failed to return: " + e.getMessage());
        }
    }

    /**
     * When the user clicks "Add Bed", this opens the AddBed page
     * @param event to add a new bed
     */

    @FXML
    void onAddBed(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/AddBed.fxml"));
            Scene scene = new Scene(loader.load(), 400, 250);

            AddBedController controller = loader.getController();
            controller.setModel(model);
            controller.setLoggedInStaff(loggedInStaff);

            // Switch the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Add Bed");
            stage.show();

            System.out.println("Switched to AddBed.fxml");

        } catch (Exception e) {
            showAlert("Failed to return: " + e.getMessage());
        }
    }

    /**
     * When the doctor clicks on "Add Prescription" on the dashboard,
     * this opens the AddPrescription page.
     * @param event The button click event
     */

    @FXML
    void onAddPrescription(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/AddPrescription.fxml"));
            Scene scene = new Scene(loader.load(), 550, 350);

            AddPrescriptionController controller = loader.getController();
            controller.setModel(model);
            controller.setLoggedInStaff(loggedInStaff);

            // Switch the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Add Prescription");
            stage.show();

            System.out.println("Switched to AddPrescription.fxml");

        } catch (Exception e) {
            showAlert("Failed to return: " + e.getMessage());
        }
    }

    /**
     * When the nurse clicks "Move Resident" on the dashboard,
     * this opens the MoveResident page.
     * @param event The button click event
     */

    @FXML
    void onMoveResident(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/MoveResident.fxml"));
            Scene scene = new Scene(loader.load(), 500, 320);

            MoveResidentController controller = loader.getController();
            controller.setModel(model);
            controller.setLoggedInStaff(loggedInStaff);

            // Switch the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Move Resident");
            stage.show();

            System.out.println("Switched to MoveResident.fxml");

        } catch (Exception e) {
            showAlert("Failed to return: " + e.getMessage());
        }
    }


    /**
     * When the user clicks "Residents" on the dashboard,
     * open the resident list page to view all current residents.
     *
     * @param event The button click event
     */

    @FXML
    void onOpenResidents(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/ResidentList.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);

            ResidentListController controller = loader.getController();
            controller.setModel(model);
            controller.setLoggedInStaff(loggedInStaff);

            // Switch the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Residents");
            stage.show();

            System.out.println("Switched to ResidentList.fxml");

        } catch (Exception e) {
            showAlert("Failed to return: " + e.getMessage());
        }
    }

    /**
     * When the manager clicks "Assign Shift" on the dashboard,
     * this button will open the AssignShift page.
     * It lets the manager assign or edit staff working shifts.
     *
     * @param event The button click event
     */

    @FXML
    void onAssignShift(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/AssignShift.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);

            AssignShiftController controller = loader.getController();
            controller.setModel(model);
            controller.setLoggedInStaff(loggedInStaff);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Assign Shift");
            stage.show();

        } catch (Exception e) {
            showAlert("Failed to open Assign Shift: " + e.getMessage());
        }
    }

    /**
     * When the manager clicks "Modify Password" on the dashboard,
     * this button opens the ModifyPassword page.
     * It allows the manager to update any staff member's login password.
     *
     * @param event The button click event
     */

    @FXML
    void onModifyPassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/ModifyPassword.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);

            ModifyPasswordController controller = loader.getController();
            controller.setModel(model);
            controller.setLoggedInStaff(loggedInStaff);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Modify Password");
            stage.show();
            System.out.println("Switched to ModifyPassword.fxml");
        } catch (Exception e) {
            showAlert("Failed to open Modify Password: " + e.getMessage());
        }
    }

    /**
     * When the nurse clicks "Administer Medicine" on the dashboard,
     * this button opens the AdministerMedicine page.
     * It is used to record when a medicine has been given to a patient.
     *
     * @param event The button click event
     */

    @FXML
    void onAdministerMedicine(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/AdministerMedicine.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);

            AdministerMedicineController controller = loader.getController();
            controller.setModel(model);
            controller.setLoggedInStaff(loggedInStaff);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - Administer Medicine");
            stage.show();
            System.out.println("Switched to AdministerMedicine.fxml");
        } catch (Exception e) {
            showAlert("Failed to open Administer Medicine: " + e.getMessage());
        }
    }

    /**
     * When the user clicks "View Logs" on the dashboard,
     * this button opens the ViewLogs page.
     * It allows staff to check all system actions recorded in the logs.
     *
     * @param event The button click event
     */

    @FXML
    void onViewLogs(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/ViewLogs.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);

            ViewLogsController controller = loader.getController();
            controller.setModel(model);
            controller.setLoggedInStaff(loggedInStaff);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("CareHome - View Logs");
            stage.show();
            System.out.println("Switched to ViewLogs.fxml");
        } catch (Exception e) {
            showAlert("Failed to open View Logs: " + e.getMessage());
        }
    }

    /**
     * Helper function to show pop-up messages.
     * @param msg The message text
     */

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }



}
