package app;

import electrostatics.SystemModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import screens.ScreenController;

public class App extends Application {
    public static SystemModel model;
    public static ScreenController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new AnchorPane());

        controller = new ScreenController(scene, getClass());

        controller.addScreen("gui", FXMLLoader.load(getClass().getResource("/gui.fxml")));
        controller.addScreen("splash", FXMLLoader.load(getClass().getResource("/splash.fxml")));
        controller.addScreen("field", FXMLLoader.load(getClass().getResource("/fieldparams.fxml")));

        controller.activate("gui");

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);

        primaryStage.setTitle("ronnisgay");
        primaryStage.setScene(scene);
        //primaryStage.getScene().getStylesheets().add(getClass().getResource("/gui.css").toExternalForm());

        primaryStage.setOnCloseRequest(e->{
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
