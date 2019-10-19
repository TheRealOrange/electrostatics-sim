package app;

import electrostatics.PotentialFieldLine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class potentialparamController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField stepadaptive;

    @FXML
    private Slider precisionadaptive_slider;

    @FXML
    private Slider stepadaptive_slider;

    @FXML
    private TextField precisionadaptive;

    @FXML
    private Slider step_slider;

    @FXML
    private TextField step;

    @FXML
    private Button cancelButton;

    @FXML
    private Button applyButton;

    @FXML
    private TextField stepcountadaptive;

    @FXML
    private Slider stepcountadaptive_slider;

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
        assert stepadaptive != null : "fx:id=\"finestepadaptive\" was not injected: check your FXML file 'potentialparams.fxml'.";
        assert precisionadaptive_slider != null : "fx:id=\"fineprecisionadaptive_slider\" was not injected: check your FXML file 'potentialparams.fxml'.";
        assert stepadaptive_slider != null : "fx:id=\"finestepadaptive_slider\" was not injected: check your FXML file 'potentialparams.fxml'.";
        assert precisionadaptive != null : "fx:id=\"fineprecisionadaptive\" was not injected: check your FXML file 'potentialparams.fxml'.";
        assert step_slider != null : "fx:id=\"finestep_slider\" was not injected: check your FXML file 'potentialparams.fxml'.";
        assert step != null : "fx:id=\"finestep\" was not injected: check your FXML file 'potentialparams.fxml'.";
        assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML file 'potentialparams.fxml'.";
        assert applyButton != null : "fx:id=\"applyButton\" was not injected: check your FXML file 'potentialparams.fxml'.";
        assert stepcountadaptive != null : "fx:id=\"stepcountadaptive\" was not injected: check your FXML file 'potentialparams.fxml'.";
        assert stepcountadaptive_slider != null : "fx:id=\"stepcountadaptive_slider\" was not injected: check your FXML file 'potentialparams.fxml'.";
        assert stepcount != null : "fx:id=\"stepcount\" was not injected: check your FXML file 'potentialparams.fxml'.";
        assert stepcount_slider != null : "fx:id=\"stepcount_slider\" was not injected: check your FXML file 'potentialparams.fxml'.";

        int[] stepcountadaptive_val = new int[]{1, 100000, 40000};
        int[] stepcount_val = new int[]{1, 100000, 40000};

        double[] precisionadaptive_val = new double[]{0.01, 10, 1};
        double[] stepadaptive_val = new double[]{0.01, 10, 1};
        double[] step_val = new double[]{0.01, 10, 0.1};

        stepcountadaptive.setTextFormatter(utility.intFormatter(stepcountadaptive_val[0], stepcountadaptive_val[1], stepcountadaptive_val[2]));
        stepcount.setTextFormatter(utility.intFormatter(stepcount_val[0], stepcount_val[1], stepcount_val[2]));

        precisionadaptive.setTextFormatter(utility.doubleFormatter(precisionadaptive_val[0], precisionadaptive_val[1], precisionadaptive_val[2]));
        stepadaptive.setTextFormatter(utility.doubleFormatter(stepadaptive_val[0], stepadaptive_val[1], stepadaptive_val[2]));

        step.setTextFormatter(utility.doubleFormatter(step_val[0], step_val[1], step_val[2]));

        utility.initSliderInt(stepcountadaptive_slider, stepcountadaptive_val[0], stepcountadaptive_val[1], 1);
        utility.initSliderInt(stepcount_slider, stepcount_val[0], stepcount_val[1], 1);

        utility.initSlider(precisionadaptive_slider, precisionadaptive_val[0], precisionadaptive_val[1], 0.01);
        utility.initSlider(stepadaptive_slider, stepadaptive_val[0], stepadaptive_val[1], 0.01);
        utility.initSlider(step_slider, step_val[0], step_val[1], 0.01);

        utility.bindInt(stepcountadaptive, stepcountadaptive_slider);
        utility.bindInt(stepcount, stepcount_slider);

        utility.bind(precisionadaptive, precisionadaptive_slider, 4);
        utility.bind(stepadaptive, stepadaptive_slider, 3);
        utility.bind(step, step_slider, 2);

        if (!Temp.reload) {
            stepcountadaptive_slider.setValue(stepcountadaptive_val[2]);
            stepcount_slider.setValue(stepcount_val[2]);

            precisionadaptive_slider.setValue(precisionadaptive_val[2]);
            stepadaptive_slider.setValue(stepadaptive_val[2]);
            step_slider.setValue(step_val[2]);
        } else {
            stepcountadaptive_slider.setValue(App.model.getConfig().getP_num_steps_adaptive());
            stepcount_slider.setValue(App.model.getConfig().getP_num_steps());

            precisionadaptive_slider.setValue(App.model.getConfig().getP_tolerance());
            stepadaptive_slider.setValue(App.model.getConfig().getP_step_amt_adaptive());
            step_slider.setValue(App.model.getConfig().getP_step_amt());
        }

        updateFields();
    }

    void updateFields() {
        PotentialFieldLine.setNum_steps_adaptive((int)stepcountadaptive_slider.getValue());
        PotentialFieldLine.setNum_steps((int)stepcount_slider.getValue());

        PotentialFieldLine.setTolerance(precisionadaptive_slider.getValue());
        PotentialFieldLine.setStep_amt_adaptive(stepadaptive_slider.getValue());
        PotentialFieldLine.setStep_amt(step_slider.getValue());
    }
}
