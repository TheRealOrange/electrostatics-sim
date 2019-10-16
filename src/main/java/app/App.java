package app;

import electrostatics.SystemModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import screens.ScreenController;

import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {
    public static SystemModel model;
    public static ScreenController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new AnchorPane());

        controller = new ScreenController(scene, getClass());

        Locale l = new Locale("en", "US");
        ResourceBundle rb = ResourceBundle.getBundle("LanguageBundle", l);
        System.out.println(rb);
        controller.addScreen("gui", FXMLLoader.load(getClass().getResource("/gui.fxml"), rb));
        controller.addScreen("splash", FXMLLoader.load(getClass().getResource("/splash.fxml"), rb));
        controller.addScreen("field", FXMLLoader.load(getClass().getResource("/fieldparams.fxml"), rb));
        controller.addScreen("potential", FXMLLoader.load(getClass().getResource("/potentialparams.fxml"), rb));

        controller.activate("gui");

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);

        primaryStage.setTitle("nonono");
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
