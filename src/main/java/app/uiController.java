package app;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

import canvas.*;
import electrostatics.ElectricFieldLine;
import electrostatics.Particle;
import electrostatics.PotentialFieldLine;
import electrostatics.SystemModel;
import elements.Charge;
import elements.Field;
import elements.Potential;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import math.AdaptiveRungeKutta;
import math.RungeKutta;
import math.Vector2D;

public class uiController {
    private ArrayList<Charge> charges;
    private ArrayList<Field> fieldlines;
    private ArrayList<Potential> potentiallines;
    private Charge selected;

    private boolean updating;

    private boolean render;
    private boolean mode;

    private boolean drawfield;
    private boolean drawpotential;

    private int locale;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private InfiniteCanvas canvas;

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
    private Button togglemode;

    @FXML
    private TitledPane viewmenu;

    @FXML
    private AnchorPane viewpane;

    @FXML
    private ComboBox<String> efieldlinestyle;

    @FXML
    private Spinner<Integer> efieldlineweight;

    @FXML
    private ComboBox<String> ufieldlinestyle;

    @FXML
    private Spinner<Integer> ufieldlineweight;

    @FXML
    private ComboBox<String> disptheme;

    @FXML
    private TitledPane settingsmenu;

    @FXML
    private AnchorPane settingspane;

    @FXML
    private Spinner<Integer> efieldlinedensity;

    @FXML
    private ComboBox<String> efieldsolver;

    @FXML
    private Button efieldparams;

    @FXML
    private Spinner<Double> uinterval;

    @FXML
    private ComboBox<String> ufieldsolver;

    @FXML
    private Button ufieldparams;

    @FXML
    private ComboBox<String> language;

    @FXML
    void addCharge(MouseEvent event) {
        if (Movable.getCurrentObject()== null && !updating) {
            Particle p = new Particle(chargeval.getValue(), Double.parseDouble(radiusval.getText()), new Vector2D(0, 0));
            App.model.addCharge(p);
            Charge charge = new Charge(p, this::compute, this::display, this::dispose, this::select);
            canvas.getChildren().add(charge);
            charge.getOnMouseClicked().handle(null);
            this.charges.add(charge);
        }
    }

    @FXML
    void efieldParams(MouseEvent event) {
        App.controller.activate("field");
    }

    @FXML
    void toggleMode(MouseEvent event) {
        if (mode) {
            togglemode.setText(App.rb.getString("edit"));
            addcharge.setDisable(false);
        }
        else {
            togglemode.setText(App.rb.getString("move"));
            addcharge.setDisable(true);
        }
        mode = !mode;
    }

    @FXML
    void forceRender(MouseEvent event) {
        render = true;
        compute();
        display();
        render = false;
    }

    @FXML
    void loadFile(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Simulation Files (*.sim)", "*.sim"));
        fileChooser.setTitle("Save file");
        fileChooser.setInitialFileName("electrostatics.sim");
        File savedFile = fileChooser.showOpenDialog(savefile.getScene().getWindow());

        if (savedFile != null) {
            try {
                App.model = new SystemModel(savedFile);
                ufieldsolver.getSelectionModel().select(0);
                ufieldsolver.getSelectionModel().select(1);
                ufieldsolver.getSelectionModel().select(App.model.getUfieldsolver_id());

                ufieldlineweight.getValueFactory().setValue(1);
                ufieldlineweight.getValueFactory().setValue(2);
                ufieldlineweight.getValueFactory().setValue(App.model.getConfig().getP_weight());

                ufieldlinestyle.getSelectionModel().select(0);
                ufieldlinestyle.getSelectionModel().select(1);
                ufieldlinestyle.getSelectionModel().select(App.model.getConfig().getP_style());

                efieldsolver.getSelectionModel().select(0);
                efieldsolver.getSelectionModel().select(1);
                efieldsolver.getSelectionModel().select(App.model.getEfieldsolver_id());

                efieldlineweight.getValueFactory().setValue(1);
                efieldlineweight.getValueFactory().setValue(2);
                efieldlineweight.getValueFactory().setValue(App.model.getConfig().getE_weight());

                efieldlinestyle.getSelectionModel().select(0);
                efieldlinestyle.getSelectionModel().select(1);
                efieldlinestyle.getSelectionModel().select(App.model.getConfig().getE_style());

                for (Charge c : this.charges){
                    canvas.getChildren().remove(c);
                }
                loadChargesFromSystem();
                boolean tmp = render;
                render = true;
                compute();
                display();
                render = tmp;
            } catch(IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void saveFile(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Simulation Files (*.sim)", "*.sim"));
        fileChooser.setTitle("Save file");
        File savedFile = fileChooser.showSaveDialog(savefile.getScene().getWindow());

        if (savedFile != null) {
            try {
                App.model.saveToFile(savedFile);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void toggleRender(MouseEvent event) {
        if (render) {
            togglerender.setText(App.rb.getString("startrender"));
            forcerender.setDisable(false);
        }
        else {
            togglerender.setText(App.rb.getString("stoprender"));
            forcerender.setDisable(true);
        }
        render = !render;
        compute();
        display();
    }

    @FXML
    void ufieldParams(MouseEvent event) {
        App.controller.activate("potential");
    }

    @FXML
    void toggleDrawField(MouseEvent event) {
        drawfield = !drawfield; boolean tmp = render;
        render = true; display(); render = tmp;
    }

    @FXML
    void toggleDrawPotential(MouseEvent event) {
        drawpotential = !drawpotential; boolean tmp = render;
        render = true; display(); render = tmp;
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
        assert togglemode != null : "fx:id=\"toggleMode\" was not injected: check your FXML file 'gui.fxml'.";
        assert viewmenu != null : "fx:id=\"viewmenu\" was not injected: check your FXML file 'gui.fxml'.";
        assert viewpane != null : "fx:id=\"viewpane\" was not injected: check your FXML file 'gui.fxml'.";
        assert efieldlinestyle != null : "fx:id=\"efieldlinestyle\" was not injected: check your FXML file 'gui.fxml'.";
        assert efieldlineweight != null : "fx:id=\"efieldlineweight\" was not injected: check your FXML file 'gui.fxml'.";
        assert ufieldlinestyle != null : "fx:id=\"ufieldlinestyle\" was not injected: check your FXML file 'gui.fxml'.";
        assert ufieldlineweight != null : "fx:id=\"ufieldlineweight\" was not injected: check your FXML file 'gui.fxml'.";
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
        if (!Temp.reload) App.model = new SystemModel();

        String methods[] = {"Euler", "Midpoint", "Heun", "Ralston", "Ralston 4", "RK 4", "SSPRK 3", "RK 3/8", "Bogacki-Shampine", "FehlBerg", "Cash-Karp", "Dormand-Prince"};

        efieldsolver.setItems(FXCollections.observableArrayList(methods));
        efieldsolver.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            if (new_value.intValue() >= 0 && new_value.intValue() <= 11) {
                System.out.println(methods[new_value.intValue()] + " selected");
                App.model.setEfieldsolver(selectSolver(App.model::solveField, new_value.intValue()));
                App.model.setEfieldsolver_id(new_value.intValue());
            }
        });

        ufieldsolver.setItems(FXCollections.observableArrayList(methods));
        ufieldsolver.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            if (new_value.intValue() >= 0 && new_value.intValue() <= 11) {
                System.out.println(methods[new_value.intValue()] + " selected");
                App.model.setUfieldsolver(selectSolver(App.model::solvePotential, new_value.intValue()));
                App.model.setUfieldsolver_id(new_value.intValue());
            }
        });

        SpinnerValueFactory factory1 = new SpinnerValueFactory.DoubleSpinnerValueFactory(-160, 160, 8, 1.6);
        chargeval.setValueFactory(factory1);

        SpinnerValueFactory factory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1, 1);
        efieldlineweight.setValueFactory(factory2);
        efieldlineweight.getValueFactory().valueProperty().addListener(e->{
            Field.setLineWeight(efieldlineweight.getValue());
        });

        SpinnerValueFactory factory3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1, 1);
        ufieldlineweight.setValueFactory(factory3);
        ufieldlineweight.getValueFactory().valueProperty().addListener(e->{
            Potential.setLineWeight(ufieldlineweight.getValue());
        });

        SpinnerValueFactory factory4 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 1, 1);
        efieldlinedensity.setValueFactory(factory4);
        efieldlinedensity.getValueFactory().valueProperty().addListener(e->{
            App.model.setLinedensity(efieldlinedensity.getValue());
            compute();
            display();
        });

        SpinnerValueFactory factory5 = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 1, 0.1, 0.01);
        uinterval.setValueFactory(factory5);
        uinterval.getValueFactory().valueProperty().addListener(e->{
            App.model.setPotentialint(uinterval.getValue());
            compute();
            display();
        });

        String[] style = { "-fx-stroke-dash-array: 1 0;", "-fx-stroke-dash-array: 12 10;", "-fx-stroke-dash-array: 2 4;" };
        double[][] stroke = {{1.0, 0.0}, {12.0, 10.0}, {2.0, 4.0}};

        efieldlinestyle.setItems(FXCollections.observableArrayList(style));
        efieldlinestyle.setCellFactory(utility.newLineCellFactory());
        efieldlinestyle.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            if (new_value.intValue() >= 0 && new_value.intValue() <= 2) {
                System.out.println(style[new_value.intValue()] + " selected");
                Field.setLineStyle(stroke[new_value.intValue()]); Field.setStyle_num(new_value.intValue());
            }
        }); efieldlinestyle.setButtonCell(efieldlinestyle.getCellFactory().call(null));

        ufieldlinestyle.setItems(FXCollections.observableArrayList(style));
        ufieldlinestyle.setCellFactory(utility.newLineCellFactory());
        ufieldlinestyle.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            if (new_value.intValue() >= 0 && new_value.intValue() <= 2) {
                System.out.println(style[new_value.intValue()] + " selected");
                Potential.setLineStyle(stroke[new_value.intValue()]); Potential.setStyle_num(new_value.intValue());
            }
        }); ufieldlinestyle.setButtonCell(ufieldlinestyle.getCellFactory().call(null));


        String[] lang = {"English", "中文"};

        language.setItems(FXCollections.observableArrayList(lang));
        language.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            if (new_value.intValue() >= 0 && new_value.intValue() <= 1 && !App.loading) {
                System.out.println(lang[new_value.intValue()] + " selected");
                Locale locale = new Locale("en", "US");;
                switch (new_value.intValue()) {
                    case 0: locale = new Locale("en", "US"); break;
                    case 1: locale = new Locale("zh", "CN"); break;
                }
                App.rb = ResourceBundle.getBundle("LanguageBundle", locale);
                this.locale = new_value.intValue();
                reload();
            }
        }); language.getSelectionModel().select(0);

        String[] theme = {"Light", "Dark"};
        disptheme.setItems(FXCollections.observableArrayList(theme));
        disptheme.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            if (new_value.intValue() >= 0 && new_value.intValue() <= 1) {
                System.out.println(theme[new_value.intValue()] + " selected");
                if (new_value.intValue() == 1) {
                    App.controller.getScreen("gui").getStyleClass().add("dark");
                    App.controller.getScreen("potential").getStyleClass().add("dark");
                    App.controller.getScreen("field").getStyleClass().add("dark");
                } else {
                    App.controller.getScreen("gui").getStyleClass().remove("dark");
                    App.controller.getScreen("potential").getStyleClass().remove("dark");
                    App.controller.getScreen("field").getStyleClass().remove("dark");
                }
            }
        }); //disptheme.getSelectionModel().select(1);

        TextFormatter<Double> textFormatter = utility.doubleFormatter(1, 100, 10);
        radiusval.setTextFormatter(textFormatter);

        this.charges = new ArrayList<>();
        this.potentiallines = new ArrayList<>();
        this.fieldlines = new ArrayList<>();
        this.updating = false;
        this.render = true;
        this.mode = false;

        this.drawpotential = true;
        this.drawfield = true;

        drawfieldlines.setSelected(true);
        drawpotentiallines.setSelected(true);

        Movable.init(canvas, new Node[]{menu, generalmenu, viewmenu, settingsmenu});
        toggleRender(null);

        radiusval.getTextFormatter().valueProperty().addListener(e -> {
            if (mode && this.selected != null) {
                editCharge.editRadius(this.selected, Double.parseDouble(radiusval.getText()));
                compute(); display();
            }
        });
        chargeval.getValueFactory().valueProperty().addListener(e -> {
            if (mode && this.selected != null) {
                editCharge.editCharge(this.selected, chargeval.getValue());
                compute(); display();
            }
        });

        ufieldlinestyle.getSelectionModel().select(0);
        ufieldsolver.getSelectionModel().select(5);
        efieldlinestyle.getSelectionModel().select(0);
        efieldsolver.getSelectionModel().select(5);

        if (Temp.reload) {
            this.fieldlines = new ArrayList<>();
            this.potentiallines = new ArrayList<>();
            this.selected = Temp.selected;
            this.updating = Temp.updating;
            this.render = Temp.render;
            this.drawfield = Temp.drawfield;
            this.drawpotential = Temp.drawpotential;
            this.mode = Temp.mode;
            this.locale = Temp.locale;

            efieldsolver.getSelectionModel().select(Temp.e_solver);
            ufieldsolver.getSelectionModel().select(Temp.p_solver);

            efieldlinestyle.getSelectionModel().select(Temp.e_style);
            Field.setLineStyle(stroke[Temp.p_style]); Field.setStyle_num(Temp.p_style);
            ufieldlinestyle.getSelectionModel().select(Temp.p_style);
            Potential.setLineStyle(stroke[Temp.p_style]); Potential.setStyle_num(Temp.p_style);

            efieldlineweight.getValueFactory().setValue(Temp.e_weight);
            Field.setLineWeight(Temp.e_weight);
            ufieldlineweight.getValueFactory().setValue(Temp.p_weight);
            Potential.setLineWeight(Temp.p_weight);

            efieldlinedensity.getValueFactory().setValue(App.model.getLinedensity());
            uinterval.getValueFactory().setValue(App.model.getPotentialint());

            language.getSelectionModel().select(this.locale);

            loadChargesFromSystem();

            settingsmenu.setExpanded(true);
            App.loading = false;
            Temp.reload = false;

            boolean tmp = this.render;
            this.render = true;
            compute();
            display();
            this.render = tmp;
        }
    }

    void loadChargesFromSystem() {
        this.charges = new ArrayList<>();
        for (Particle p : App.model.getCharges()) {
            Charge charge = new Charge(p, this::compute, this::display, this::dispose, this::select);
            canvas.getChildren().add(charge);
            this.charges.add(charge);
        }
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

    public boolean dispose(Charge c) {
        Vector2D pos = c.getPos();
        if (menu.intersects(pos.getX(), pos.getY(), 1, 1)) {
            App.model.removeCharge(c.getCharge());
            canvas.getChildren().remove(c);
            charges.remove(c);
        }
        return true;
    }

    public boolean select(Charge c) {
        if (this.mode) {
            this.selected = c;
            radiusval.setText(String.format("%.1f", c.getCharge().getRadius()));
            chargeval.getValueFactory().setValue(c.getCharge().getCharge());
            return true;
        }
        else this.selected = null;
        return false;
    }

    void compute() {
        if (render) {
            this.updating = true;
            try {
                App.model.compute();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            this.updating = false;
        }
    }

    void display() {
        this.updating = true;
        synchronized (canvas.getChildren()) {
            for (Potential p : this.potentiallines) canvas.getChildren().remove(p);
            for (Field p : this.fieldlines) canvas.getChildren().remove(p);
            Potential.clear();
            Field.clear();

            this.potentiallines = new ArrayList<>();
            this.fieldlines = new ArrayList<>();
            if (render) {
                if (drawpotential) for (PotentialFieldLine pfl : App.model.getPotentialLines()) {
                    Potential p = new Potential(pfl);
                    canvas.getChildren().add(p);
                    p.draw();
                    potentiallines.add(p);
                }

                if (drawfield) for (ElectricFieldLine efl : App.model.getFieldLines()) {
                    Field f = new Field(efl);
                    canvas.getChildren().add(f);
                    f.draw();
                    fieldlines.add(f);
                }
            }
            this.updating = false;
        }
    }

    void reload() {
        App.loading = true;
        Temp.reload = true;

        Temp.fieldlines = this.fieldlines;
        Temp.potentiallines = this.potentiallines;
        Temp.selected = this.selected;
        Temp.updating = this.updating;
        Temp.render = this.render;
        Temp.drawfield = this.drawfield;
        Temp.drawpotential = this.drawpotential;
        Temp.mode = this.mode;
        Temp.locale = this.locale;

        Temp.e_solver = efieldsolver.getSelectionModel().getSelectedIndex();
        Temp.p_solver = ufieldsolver.getSelectionModel().getSelectedIndex();

        Temp.e_style = efieldlinestyle.getSelectionModel().getSelectedIndex();
        Temp.p_style = ufieldlinestyle.getSelectionModel().getSelectedIndex();

        Temp.e_weight = efieldlineweight.getValue();
        Temp.p_weight = ufieldlineweight.getValue();

        App.controller.removeScreen("gui");
        App.controller.removeScreen("field");
        App.controller.removeScreen("potential");
        try {
            App.controller.addScreen("gui", FXMLLoader.load(getClass().getResource("/gui.fxml"), App.rb));
            App.controller.addScreen("field", FXMLLoader.load(getClass().getResource("/fieldparams.fxml"), App.rb));
            App.controller.addScreen("potential", FXMLLoader.load(getClass().getResource("/potentialparams.fxml"), App.rb));
        } catch (IOException e) {
            e.printStackTrace();
        }
        App.controller.activate("gui");
    }
}
