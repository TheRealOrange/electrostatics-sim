package app;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import electrostatics.ElectricFieldLine;
import electrostatics.Particle;
import electrostatics.PotentialFieldLine;
import electrostatics.SystemModel;
import elements.LineAnimation;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.util.Callback;
import math.RungeKutta;
import math.Vector2D;

public class splashController {
    SystemModel model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane canvas;

    @FXML
    private Text title;

    @FXML
    private Label loading;

    @FXML
    void initialize() {
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'splash.fxml'.";
        assert title != null : "fx:id=\"title\" was not injected: check your FXML file 'splash.fxml'.";
        assert loading != null : "fx:id=\"loading\" was not injected: check your FXML file 'splash.fxml'.";

        model = new SystemModel();
        Particle p = new Particle(8, 70, new Vector2D(457 / 2, 324 / 2));
        Circle c = new Circle();
        c.setFill(Color.rgb(112, 112, 255));
        c.setRadius(p.getRadius());
        c.setCenterX(p.getPosition().getX());
        c.setCenterY(p.getPosition().getY());
        model.addCharge(p);
        canvas.getChildren().add(c);
        c.toBack();

        model.setEfieldsolver(new RungeKutta.SSPRK3(model::solveField));
        model.setUfieldsolver(new RungeKutta.SSPRK3(model::solvePotential));

        model.setPotentialint(0.01);

        compute();
        display();

    }

    void loadGUI() {
        try {
            App.loading = true;
            App.controller.addScreen("gui", FXMLLoader.load(getClass().getResource("/gui.fxml"), App.rb));
            App.controller.addScreen("field", FXMLLoader.load(getClass().getResource("/fieldparams.fxml"), App.rb));
            App.controller.addScreen("potential", FXMLLoader.load(getClass().getResource("/potentialparams.fxml"), App.rb));
            App.loading = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.controller.activate("gui");
        App.primarystage.setMinWidth(800);
        App.primarystage.setMinHeight(700);
        App.primarystage.setResizable(true);
    }

    void compute() {
        try {
            model.compute();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    void display() {
        ArrayList<AnimationTimer> animate = new ArrayList<>();
        final int[] total = {model.getPotentialLines().size() + model.getFieldLines().size() - 1};
        final int[] num = {0};

        for (PotentialFieldLine pfl : model.getPotentialLines()) {
            LineAnimation l = new LineAnimation(pfl.getPoints(), 80, e -> {
                synchronized (num) {
                    ++num[0];
                    if (num[0] == total[0]) loadGUI();
                }
                return null;
            });
            l.getP().getStrokeDashArray().addAll(4.0, 4.0);
            canvas.getChildren().add(l.getP());
            l.getP().toBack();
            animate.add(l);
        }

        for (ElectricFieldLine efl : model.getFieldLines()) {
            LineAnimation l = new LineAnimation(efl.getPoints(), 80, e -> {
                synchronized (num) {
                    ++num[0];
                    if (num[0] == total[0]) loadGUI();
                }
                return null;
            });
            canvas.getChildren().add(l.getP());
            l.getP().toBack();
            animate.add(l);
        }

        for (AnimationTimer at : animate) at.start();
    }
}
