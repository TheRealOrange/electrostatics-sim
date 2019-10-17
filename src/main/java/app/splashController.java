package app;

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
import javafx.scene.text.Text;
import math.RungeKutta;
import math.Vector2D;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class splashController {
    SystemModel model;
    private Random r = new Random();
    private long prevTime;
    private Boolean load = true;
    private String[] loadingme = { "obliterating enemies...", "git push --force", "solving navier-stokes...", "proving riemann hypothesis...", "loading...", "eating cookies...", "catching up on sleep...", "burden bear...", "inventing baryons...", "fixing quantum gravity...", "simulating universe...", "failing cs...", "splitting the atom...", "growing potatoes for space colonization...", "making paperclips..." };

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

    private void loadGUI() {
        load = false;
        try {
            App.loading = true;
            App.controller.addScreen("gui", FXMLLoader.load(getClass().getResource("/gui.fxml"), App.rb));
            App.controller.addScreen("field", FXMLLoader.load(getClass().getResource("/fieldparams.fxml"), App.rb));
            App.controller.addScreen("potential", FXMLLoader.load(getClass().getResource("/potentialparams.fxml"), App.rb));
            App.settheme.accept(0);
            App.loading = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.controller.activate("gui");
        App.primarystage.setMinWidth(800);
        App.primarystage.setMinHeight(700);
        App.primarystage.setResizable(true);
    }

    private void compute() {
        try {
            model.compute();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void updateLoadingMessage() {
        long time = System.currentTimeMillis();
        if (time - prevTime > 1200) {
            loading.setText(loadingme[r.nextInt(loadingme.length)]);
            prevTime = time;
        }
    }

    private void display() {
        ArrayList<AnimationTimer> animate = new ArrayList<>();
        final int[] total = {model.getPotentialLines().size() + model.getFieldLines().size() - 2};
        final int[] num = {0};

        animate.add(new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateLoadingMessage();
                if (num[0] == total[0]) this.stop();
            }
        });

        for (PotentialFieldLine pfl : model.getPotentialLines()) {
            LineAnimation l = new LineAnimation(pfl.getPoints(), 50, pfl.getPoints().size(), e -> {
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
            LineAnimation l = new LineAnimation(efl.getPoints(), 10, efl.getPoints().size()/3, e -> {
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
