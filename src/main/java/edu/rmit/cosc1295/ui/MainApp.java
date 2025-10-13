package edu.rmit.cosc1295.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Super tiny JavaFX app just to see a window.
 * We'll wire this to our CareHome later (login/dashboard etc).
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Button btn = new Button("Hello JavaFX 👋");
        btn.setOnAction(e -> System.out.println("Button clicked!"));

        StackPane root = new StackPane(btn);
        Scene scene = new Scene(root, 480, 320);

        stage.setTitle("CareHome - JavaFX bootstrap");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // JavaFX entry
    }
}
