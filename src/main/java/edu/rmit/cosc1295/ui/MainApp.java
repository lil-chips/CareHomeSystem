package edu.rmit.cosc1295.ui;

import edu.rmit.cosc1295.carehome.CareHome;
import javafx.application.Application;
import javafx.stage.*;

/**
 * Entry point of the app
 */
public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        CareHome app = new CareHome();
    }
}