package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.CareHome;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.*;

/**
 * Entry point of the app
 */
public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        CareHome app = new CareHome();

        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/rmit/cosc1295/ui/login.fxml"));
        Scene scene = new Scene(loader.load(), 520, 360);

        // Get the controller object that is linked to login.fxml
        LoginController controller = loader.getController();
        // Give the controller access to the CareHome model
        controller.setModel(app);

        // Configure and show the stage
        stage.setTitle("CareHome System - Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // Launch JavaFX app
    public static void main(String[] args) {
        launch(args);
    }
}