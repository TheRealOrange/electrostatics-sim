package app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

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

    }
}
