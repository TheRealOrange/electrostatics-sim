package app;

import electrostatics.ElectricFieldLine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

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

        int[] resolutionthreshold_val = new int[]{1, 10000, 1000};
        int[] stepcount_val = new int[]{1, 100000, 10000};

        double[] fineprecisionadaptive_val = new double[]{0.1, 100, 1};
        double[] finestepadaptive_val = new double[]{0.1, 100, 1};
        double[] finestep_val = new double[]{0.1, 100, 15};

        double[] roughprecisionadaptive_val = new double[]{0.1, 1000, 50};
        double[] roughstepadaptive_val = new double[]{0.1, 1000, 50};
        double[] roughstep_val = new double[]{0.1, 1000, 50};

        resolutionthreshold.setTextFormatter(utility.intFormatter(resolutionthreshold_val[0], resolutionthreshold_val[1], resolutionthreshold_val[2]));
        stepcount.setTextFormatter(utility.intFormatter(stepcount_val[0], stepcount_val[1], stepcount_val[2]));

        fineprecisionadaptive.setTextFormatter(utility.doubleFormatter(fineprecisionadaptive_val[0], fineprecisionadaptive_val[1], fineprecisionadaptive_val[2]));
        finestepadaptive.setTextFormatter(utility.doubleFormatter(finestepadaptive_val[0], finestepadaptive_val[1], finestepadaptive_val[2]));

        finestep.setTextFormatter(utility.doubleFormatter(finestep_val[0], finestep_val[1], finestep_val[2]));

        roughprecisionadaptive.setTextFormatter(utility.doubleFormatter(roughprecisionadaptive_val[0], roughprecisionadaptive_val[1], roughprecisionadaptive_val[2]));
        roughstepadaptive.setTextFormatter(utility.doubleFormatter(roughstepadaptive_val[0], roughstepadaptive_val[1], roughstepadaptive_val[2]));

        roughstep.setTextFormatter(utility.doubleFormatter(roughstep_val[0], roughstep_val[1], roughstep_val[2]));

        utility.initSliderInt(resolutionthreshold_slider, resolutionthreshold_val[0], resolutionthreshold_val[1], 1);
        utility.initSliderInt(stepcount_slider, stepcount_val[0], stepcount_val[1], 1);

        utility.initSlider(fineprecisionadaptive_slider, fineprecisionadaptive_val[0], fineprecisionadaptive_val[1], 0.1);
        utility.initSlider(finestepadaptive_slider, finestepadaptive_val[0], finestepadaptive_val[1], 0.1);
        utility.initSlider(finestep_slider, finestep_val[0], finestep_val[1], 0.1);

        utility.initSlider(roughprecisionadaptive_slider, roughprecisionadaptive_val[0], roughprecisionadaptive_val[1], 0.1);
        utility.initSlider(roughstepadaptive_slider, roughstepadaptive_val[0], roughstepadaptive_val[1], 0.1);
        utility.initSlider(roughstep_slider, roughstep_val[0], roughstep_val[1], 0.1);

        utility.bindInt(resolutionthreshold, resolutionthreshold_slider);
        utility.bindInt(stepcount, stepcount_slider);

        utility.bind(fineprecisionadaptive, fineprecisionadaptive_slider, 1);
        utility.bind(finestepadaptive, finestepadaptive_slider, 1);
        utility.bind(finestep, finestep_slider, 1);

        utility.bind(roughprecisionadaptive, roughprecisionadaptive_slider, 1);
        utility.bind(roughstepadaptive, roughstepadaptive_slider, 1);
        utility.bind(roughstep, roughstep_slider, 1);

        resolutionthreshold_slider.setValue(resolutionthreshold_val[2]);
        stepcount_slider.setValue(stepcount_val[2]);

        fineprecisionadaptive_slider.setValue(fineprecisionadaptive_val[2]);
        finestepadaptive_slider.setValue(finestepadaptive_val[2]);
        finestep_slider.setValue(finestep_val[2]);

        roughprecisionadaptive_slider.setValue(roughprecisionadaptive_val[2]);
        roughstepadaptive_slider.setValue(roughstepadaptive_val[2]);
        roughstep_slider.setValue(roughstep_val[2]);

        updateFields();
    }

    private void updateFields() {
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