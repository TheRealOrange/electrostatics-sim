package app;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import electrostatics.ElectricField;
import electrostatics.ElectricFieldLine;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

public class efieldparamController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField resolutionthreshold;

    @FXML
    private TextField roughprecisionadaptive;

    @FXML
    private TextField finestepadaptive;

    @FXML
    private Slider resolutionthreshold_slider;

    @FXML
    private Slider fineprecisionadaptive_slider;

    @FXML
    private Slider finestepadaptive_slider;

    @FXML
    private TextField roughstepadaptive;

    @FXML
    private TextField fineprecisionadaptive;

    @FXML
    private Slider roughprecisionadaptive_slider;

    @FXML
    private Slider roughstepadaptive_slider;

    @FXML
    private Slider roughstep_slider;

    @FXML
    private Slider finestep_slider;

    @FXML
    private TextField finestep;

    @FXML
    private TextField roughstep;

    @FXML
    private Button cancelButton;

    @FXML
    private Button applyButton;

    @FXML
    private TextField stepcount;

    @FXML
    private Slider stepcount_slider;


    @FXML
    void applyClicked(ActionEvent event) {
        updateFields();
        App.controller.activate("gui");
    }

    @FXML
    void cancelClicked(ActionEvent event) {
        App.controller.activate("gui");
    }

    @FXML
    void initialize() {
        assert resolutionthreshold != null : "fx:id=\"resolutionthreshold\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert roughprecisionadaptive != null : "fx:id=\"roughprecisionadaptive\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert finestepadaptive != null : "fx:id=\"finestepadaptive\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert resolutionthreshold_slider != null : "fx:id=\"resolutionthreshold_slider\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert fineprecisionadaptive_slider != null : "fx:id=\"fineprecisionadaptive_slider\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert finestepadaptive_slider != null : "fx:id=\"finestepadaptive_slider\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert roughstepadaptive != null : "fx:id=\"roughstepadaptive\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert fineprecisionadaptive != null : "fx:id=\"fineprecisionadaptive\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert roughprecisionadaptive_slider != null : "fx:id=\"roughprecisionadaptive_slider\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert roughstepadaptive_slider != null : "fx:id=\"roughstepadaptive_slider\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert roughstep_slider != null : "fx:id=\"roughstep_slider\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert finestep_slider != null : "fx:id=\"finestep_slider\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert finestep != null : "fx:id=\"finestep\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert roughstep != null : "fx:id=\"roughstep\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert applyButton != null : "fx:id=\"applyButton\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert stepcount != null : "fx:id=\"stepcount\" was not injected: check your FXML file 'fieldparams.fxml'.";
        assert stepcount_slider != null : "fx:id=\"stepcount_slider\" was not injected: check your FXML file 'fieldparams.fxml'.";

        Pattern validEditingState = Pattern.compile("(([1-9][0-9]*)|0)?(\\.[0-9]*)?");
        Pattern validEditingStateInt = Pattern.compile("[\\d]*");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) return c;
            else return null;
        };

        UnaryOperator<TextFormatter.Change> filterInt = c -> {
            String text = c.getControlNewText();
            if (validEditingStateInt.matcher(text).matches()) return c;
            else return null;
        };

        StringConverter<Double> fine = new StringConverter<>() {
            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || ".".equals(s) || Double.parseDouble(s) < 0.1 || Double.parseDouble(s) > 100) return 1.0;
                else return Double.valueOf(s);
            }
            @Override
            public String toString(Double d) { return d.toString(); }
        };

        StringConverter<Double> rough = new StringConverter<>() {
            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || ".".equals(s) || Double.parseDouble(s) < 0.1 || Double.parseDouble(s) > 1000) return 50.0;
                else return Double.valueOf(s);
            }
            @Override
            public String toString(Double d) { return d.toString(); }
        };

        StringConverter<Integer> integer1 = new StringConverter<>() {
            @Override
            public Integer fromString(String s) {
                if (s.isEmpty() || Integer.parseInt(s) < 1 || Integer.parseInt(s) > 100000) return 1000;
                else return Integer.valueOf(s);
            }
            @Override
            public String toString(Integer d) { return d.toString(); }
        };

        StringConverter<Integer> integer2 = new StringConverter<>() {
            @Override
            public Integer fromString(String s) {
                if (s.isEmpty() || Integer.parseInt(s) < 1 || Integer.parseInt(s) > 10000) return 1000;
                else return Integer.valueOf(s);
            }
            @Override
            public String toString(Integer d) { return d.toString(); }
        };

        resolutionthreshold.setTextFormatter(new TextFormatter<Integer>(integer2, 100, filterInt));
        stepcount.setTextFormatter(new TextFormatter<Integer>(integer1, 10000, filterInt));

        fineprecisionadaptive.setTextFormatter(new TextFormatter<Double>(fine, 1.0, filter));
        finestepadaptive.setTextFormatter(new TextFormatter<Double>(fine, 1.0, filter));

        finestep.setTextFormatter(new TextFormatter<Double>(fine, 15.0, filter));

        roughprecisionadaptive.setTextFormatter(new TextFormatter<Double>(rough, 10.0, filter));
        roughstepadaptive.setTextFormatter(new TextFormatter<Double>(rough, 10.0, filter));

        roughstep.setTextFormatter(new TextFormatter<Double>(rough, 50.0, filter));

        initSliderInt(resolutionthreshold_slider, 1, 10000, 1);
        initSliderInt(stepcount_slider, 1, 100000, 1);

        initSlider(fineprecisionadaptive_slider, 0.1, 100, 0.1);
        initSlider(finestepadaptive_slider, 0.1, 100, 0.1);
        initSlider(finestep_slider, 0.1, 100, 0.1);

        initSlider(roughprecisionadaptive_slider, 0.1, 1000, 0.1);
        initSlider(roughstepadaptive_slider, 0.1, 1000, 0.1);
        initSlider(roughstep_slider, 0.1, 1000, 0.1);

        bindInt(resolutionthreshold, resolutionthreshold_slider);
        bindInt(stepcount, stepcount_slider);

        bind(fineprecisionadaptive, fineprecisionadaptive_slider);
        bind(finestepadaptive, finestepadaptive_slider);
        bind(finestep, finestep_slider);

        bind(roughprecisionadaptive, roughprecisionadaptive_slider);
        bind(roughstepadaptive, roughstepadaptive_slider);
        bind(roughstep, roughstep_slider);

        resolutionthreshold_slider.setValue(100);
        stepcount_slider.setValue(10000);

        fineprecisionadaptive_slider.setValue(1);
        finestepadaptive_slider.setValue(1);
        finestep_slider.setValue(15);

        roughprecisionadaptive_slider.setValue(10);
        roughstepadaptive_slider.setValue(10);
        roughstep_slider.setValue(50);

        updateFields();
    }

    void initSlider(Slider slider, double min, double max, double step) {
        slider.setMin(min);
        slider.setMax(max);
        slider.setMajorTickUnit(step);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);
    }

    void initSliderInt(Slider slider, int min, int max, int step) {
        slider.setMin(min);
        slider.setMax(max);
        slider.setMajorTickUnit(step);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);
    }

    void bind(TextField tf, Slider s) {
        tf.setOnAction(e->s.setValue(Double.parseDouble(tf.getText())));
        s.valueProperty().addListener((b,o,n)->tf.setText(String.format("%.2f", n)));
    }

    void bindInt(TextField tf, Slider s) {
        tf.setOnAction(e->s.setValue(Integer.parseInt(tf.getText())));
        s.valueProperty().addListener((b,o,n)->tf.setText(String.format("%d", n.intValue())));
    }

    void updateFields() {
        ElectricFieldLine.setFine_compute_distance((int)resolutionthreshold_slider.getValue());
        ElectricFieldLine.setNum_steps((int)stepcount_slider.getValue());

        ElectricFieldLine.setFine_precision_adaptive(fineprecisionadaptive_slider.getValue());
        ElectricFieldLine.setFine_step_adaptive(finestepadaptive_slider.getValue());
        ElectricFieldLine.setFine_step(finestep_slider.getValue());

        ElectricFieldLine.setRough_precision_adaptive(roughstepadaptive_slider.getValue());
        ElectricFieldLine.setRough_step_adaptive(roughstepadaptive_slider.getValue());
        ElectricFieldLine.setRough_step(roughstep_slider.getValue());
    }
}