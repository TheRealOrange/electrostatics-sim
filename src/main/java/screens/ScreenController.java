package screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;

//class to set the main screen of the UI and store and manage multiple screens

public class ScreenController {
    private HashMap<String, Pane> screens = new HashMap<>();
    private Scene main;
    private Class loader;

    public ScreenController(Scene main, Class<?> aClass) {
        this.main = main;
        this.loader = aClass;
    }

    public void addScreen(String name, Pane pane) {
        screens.put(name, pane);
    }

    public Object addScreen(String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.loader.getResource("/" + name + ".fxml"));
        addScreen(name, loader.load());
        screens.get(name).getStylesheets().add(getClass().getResource("/gui.css").toExternalForm());
        return loader.getController();
    }

    public void removeScreen(String name) {
        screens.remove(name);
    }

    public void activate(String name) {
        main.setRoot(screens.get(name));
    }
    public Pane getScreen(String name) { return screens.get(name); }
}