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
    public static ResourceBundle rb;

    public static Stage primarystage;

    public static boolean loading;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new AnchorPane());

        controller = new ScreenController(scene, getClass());
        loading = false;

        Locale l = new Locale("en", "US");
        rb = ResourceBundle.getBundle("LanguageBundle", l);
        System.out.println(rb);
        controller.addScreen("splash", FXMLLoader.load(getClass().getResource("/splash.fxml"), rb));

        controller.activate("splash");

        primaryStage.setMinHeight(457);
        primaryStage.setMinHeight(324);

        primaryStage.setResizable(false);
        primarystage = primaryStage;

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
