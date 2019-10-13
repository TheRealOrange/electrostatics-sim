package app;

import java.awt.MouseInfo;
import java.awt.Point;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import electrostatics.ElectricFieldLine;
import electrostatics.Particle;
import electrostatics.PotentialFieldLine;
import electrostatics.SystemModel;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import math.AdaptiveRungeKutta;
import math.RungeKutta;
import math.Vector2D;

public class uiController {
    private ArrayList<Charge> charges;
    private ArrayList<Field> fieldlines;
    private ArrayList<Potential> potentiallines;
    private double scaling;
    private AnimationTimer timer;

    private boolean ismoving;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane canvas;

    @FXML
    private Accordion menu;

    @FXML
    private TitledPane generalmenu;

    @FXML
    private AnchorPane generalpane;

    @FXML
    private Button togglerender;

    @FXML
    private Button forcerender;

    @FXML
    private Button savefile;

    @FXML
    private Button loadfile;

    @FXML
    private Button addcharge;

    @FXML
    private Spinner<Double> chargeval;

    @FXML
    private CheckBox drawfieldlines;

    @FXML
    private CheckBox drawpotentiallines;

    @FXML
    private TextField radiusval;

    @FXML
    private TitledPane viewmenu;

    @FXML
    private AnchorPane viewpane;

    @FXML
    private ComboBox<?> efieldlinestyle;

    @FXML
    private Spinner<?> efieldlineweight;

    @FXML
    private ComboBox<?> ufieldlinestyle;

    @FXML
    private Spinner<?> ufieldlineweight;

    @FXML
    private Spinner<?> dispscale;

    @FXML
    private ComboBox<?> disptheme;

    @FXML
    private TitledPane settingsmenu;

    @FXML
    private AnchorPane settingspane;

    @FXML
    private Spinner<?> efieldlinedensity;

    @FXML
    private ComboBox<String> efieldsolver;

    @FXML
    private Button efieldparams;

    @FXML
    private Spinner<?> uinterval;

    @FXML
    private ComboBox<String> ufieldsolver;

    @FXML
    private Button ufieldparams;

    @FXML
    private ComboBox<?> language;

    @FXML
    void addCharge(MouseEvent event) {
        if (!ismoving) {
            Particle p = new Particle(chargeval.getValue(), Double.parseDouble(radiusval.getText()), new Vector2D(0, 0));
            Charge charge = new Charge(p);
            App.model.addCharge(p);
            App.controller.getScreen("gui").getChildren().add(charge);

            charge.setOnMouseClicked(e -> {
                if (charge.isMove() && ismoving) {
                    timer.stop();
                    charge.setMove(false);
                    ismoving = false;
                    App.model.moveParticle(charge.getCharge(), charge.getPos());
                    System.out.printf("x: %f, y: %f\n", charge.getPos().getX(), charge.getPos().getY());
                    compute();
                    display();
                } else if (!ismoving) {
                    charge.setMove(true);
                    ismoving = true;
                    timer = new AnimationTimer() {
                        @Override
                        public void handle(long now) {
                            Point mouse = normalizedMousePos();
                            if (!App.model.checkMoveCollision(charge.getCharge(), new Vector2D(mouse))) {
                                charge.setPos(new Vector2D(mouse));
                            }
                        }
                    };
                    timer.start();
                }
            });

            charge.getOnMouseClicked().handle(null);
        }
    }

    @FXML
    void efieldParams(MouseEvent event) {
        App.controller.activate("field");
    }

    @FXML
    void forceRender(MouseEvent event) {

    }

    @FXML
    void loadFile(MouseEvent event) {

    }

    @FXML
    void saveFile(MouseEvent event) {

    }

    @FXML
    void moveCanvas(MouseEvent event) {

    }

    @FXML
    void toggleRender(MouseEvent event) {

    }

    @FXML
    void ufieldParams(MouseEvent event) {

    }

    @FXML
    void initialize() {
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'gui.fxml'.";
        assert menu != null : "fx:id=\"menu\" was not injected: check your FXML file 'gui.fxml'.";
        assert generalmenu != null : "fx:id=\"generalmenu\" was not injected: check your FXML file 'gui.fxml'.";
        assert generalpane != null : "fx:id=\"generalpane\" was not injected: check your FXML file 'gui.fxml'.";
        assert togglerender != null : "fx:id=\"togglerender\" was not injected: check your FXML file 'gui.fxml'.";
        assert forcerender != null : "fx:id=\"forcerender\" was not injected: check your FXML file 'gui.fxml'.";
        assert savefile != null : "fx:id=\"savefile\" was not injected: check your FXML file 'gui.fxml'.";
        assert loadfile != null : "fx:id=\"loadfile\" was not injected: check your FXML file 'gui.fxml'.";
        assert addcharge != null : "fx:id=\"addcharge\" was not injected: check your FXML file 'gui.fxml'.";
        assert chargeval != null : "fx:id=\"chargeval\" was not injected: check your FXML file 'gui.fxml'.";
        assert drawfieldlines != null : "fx:id=\"drawfieldlines\" was not injected: check your FXML file 'gui.fxml'.";
        assert drawpotentiallines != null : "fx:id=\"drawpotentiallines\" was not injected: check your FXML file 'gui.fxml'.";
        assert radiusval != null : "fx:id=\"radiusval\" was not injected: check your FXML file 'gui.fxml'.";
        assert viewmenu != null : "fx:id=\"viewmenu\" was not injected: check your FXML file 'gui.fxml'.";
        assert viewpane != null : "fx:id=\"viewpane\" was not injected: check your FXML file 'gui.fxml'.";
        assert efieldlinestyle != null : "fx:id=\"efieldlinestyle\" was not injected: check your FXML file 'gui.fxml'.";
        assert efieldlineweight != null : "fx:id=\"efieldlineweight\" was not injected: check your FXML file 'gui.fxml'.";
        assert ufieldlinestyle != null : "fx:id=\"ufieldlinestyle\" was not injected: check your FXML file 'gui.fxml'.";
        assert ufieldlineweight != null : "fx:id=\"ufieldlineweight\" was not injected: check your FXML file 'gui.fxml'.";
        assert dispscale != null : "fx:id=\"dispscale\" was not injected: check your FXML file 'gui.fxml'.";
        assert disptheme != null : "fx:id=\"disptheme\" was not injected: check your FXML file 'gui.fxml'.";
        assert settingsmenu != null : "fx:id=\"settingsmenu\" was not injected: check your FXML file 'gui.fxml'.";
        assert settingspane != null : "fx:id=\"settingspane\" was not injected: check your FXML file 'gui.fxml'.";
        assert efieldlinedensity != null : "fx:id=\"efieldlinedensity\" was not injected: check your FXML file 'gui.fxml'.";
        assert efieldsolver != null : "fx:id=\"efieldsolver\" was not injected: check your FXML file 'gui.fxml'.";
        assert efieldparams != null : "fx:id=\"efieldparams\" was not injected: check your FXML file 'gui.fxml'.";
        assert uinterval != null : "fx:id=\"uinterval\" was not injected: check your FXML file 'gui.fxml'.";
        assert ufieldsolver != null : "fx:id=\"ufieldsolver\" was not injected: check your FXML file 'gui.fxml'.";
        assert ufieldparams != null : "fx:id=\"ufieldparams\" was not injected: check your FXML file 'gui.fxml'.";
        assert language != null : "fx:id=\"language\" was not injected: check your FXML file 'gui.fxml'.";

        String methods[] = {"Euler", "Midpoint", "Heun", "Ralston", "Ralston 4", "RK 4", "SSPRK 3", "RK 3/8", "Bogacki-Shampine", "FehlBerg", "Cash-Karp", "Dormand-Prince"};

        efieldsolver.setItems(FXCollections.observableArrayList(methods));
        efieldsolver.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            if (new_value.intValue() >= 0 && new_value.intValue() <= 11) {
                System.out.println(methods[new_value.intValue()] + " selected");
                App.model.setEfieldsolver(selectSolver(App.model::solveField, new_value.intValue()));
            }
        });

        ufieldsolver.setItems(FXCollections.observableArrayList(methods));
        ufieldsolver.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            if (new_value.intValue() >= 0 && new_value.intValue() <= 11) {
                System.out.println(methods[new_value.intValue()] + " selected");
                App.model.setUfieldsolver(selectSolver(App.model::solvePotential, new_value.intValue()));
            }
        });

        SpinnerValueFactory factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(-160, 160, 1.6, 1.6);

        chargeval.setValueFactory(factory);

        Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) return c;
            else return null;
        };

        StringConverter<Double> converter = new StringConverter<>() {
            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s) || Double.valueOf(s) <= 0 || Double.valueOf(s) >= 100) return 10.0 ;
                else return Double.valueOf(s);
            }
            @Override
            public String toString(Double d) { return d.toString(); }
        };

        TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 10.0, filter);
        radiusval.setTextFormatter(textFormatter);

        App.model = new SystemModel();
        ismoving = false;

        this.potentiallines = new ArrayList<>();
        this.fieldlines = new ArrayList<>();
    }

    RungeKutta selectSolver(BiFunction<Double, Vector2D, Vector2D> func, int solver) {
        switch (solver) {
            case 0: return new RungeKutta.Euler(func);
            case 1: return new RungeKutta.Midpoint(func);
            case 2: return new RungeKutta.Heun(func);
            case 3: return new RungeKutta.Ralston(func);
            case 4: return new RungeKutta.Ralston4(func);
            case 5: return new RungeKutta.RK4(func);
            case 6: return new RungeKutta.SSPRK3(func);
            case 7: return new RungeKutta.RK3_8(func);
            case 8: return new AdaptiveRungeKutta.BogackiShampine(func);
            case 9: return new AdaptiveRungeKutta.FehlBerg(func);
            case 10: return new AdaptiveRungeKutta.CashKarp(func);
            case 11: return new AdaptiveRungeKutta.DormandPrince(func);
        }
        return null;
    }

    void updateChargePosition() {
        for (Charge c : charges) {
            c.setPos(c.getCharge().getPosition().sub(App.model.getOrigin()).mul(scaling));
        }
    }

    public Vector2D collision(Vector2D pos) {
        final Bounds bounds = App.controller.getScreen("gui").localToScreen(App.controller.getScreen("gui").getBoundsInLocal());
        if (Math.abs(bounds.getMinX()-pos.getX()) < 10 || Math.abs(bounds.getMinY()-pos.getY()) < 10 || Math.abs(bounds.getMaxX()-pos.getX()) < 10 || Math.abs(bounds.getMaxY()-pos.getY()) < 10) {
            return pos.div(pos.magnitude());
        }
        return null;
    }

    public Point normalizedMousePos() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        Bounds bounds = App.controller.getScreen("gui").localToScreen(App.controller.getScreen("gui").getBoundsInLocal());
        p.setLocation((p.getX() < bounds.getMinX()+10)?10:p.getX()-bounds.getMinX(),(p.getY() < bounds.getMinY()+10)?10:p.getY()-bounds.getMinY());
        p.setLocation((p.getX() > bounds.getWidth()-10)?bounds.getWidth()-10:p.getX(),(p.getY() > bounds.getHeight()-10)?bounds.getHeight()-10:p.getY());
        return p;
    }

    void compute() {
        try {
            App.model.compute();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void display() {
        for (Potential p : this.potentiallines) canvas.getChildren().remove(p);
        for (Field p : this.fieldlines) canvas.getChildren().remove(p);

        this.potentiallines = new ArrayList<>();
        this.fieldlines = new ArrayList<>();

        for (PotentialFieldLine pfl : App.model.getPotentialLines()) {
            Potential p = new Potential(pfl);
            canvas.getChildren().add(p);
            p.draw();
            potentiallines.add(p);
        }

        for (ElectricFieldLine efl : App.model.getFieldLines()) {
            Field f = new Field(efl);
            canvas.getChildren().add(f);
            f.draw();
            fieldlines.add(f);
        }
    }
}
