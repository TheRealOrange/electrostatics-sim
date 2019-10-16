package app;

import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public abstract class utility {
    static Callback<ListView<String>, ListCell<String>> newLineCellFactory() {
        return new Callback<>() {
            @Override public ListCell<String> call(ListView<String> p) {
                return new ListCell<>() {
                    private final Group group;
                    private final Line line;{
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        group = new Group();
                        group.getChildren().add(new Rectangle(80, 15, Color.TRANSPARENT));
                        line = new Line(0, 7.5, 100, 7.5);
                        group.getChildren().add(line);
                    }

                    @Override protected void updateItem(String style, boolean empty) {
                        super.updateItem(style, empty);
                        if(style != null && !empty) {
                            line.setStyle(style);
                            setGraphic(group);
                        }
                    }
                };
            }
        };
    }

    static void initSlider(Slider slider, double min, double max, double step) {
        slider.setMin(min);
        slider.setMax(max);
        slider.setMajorTickUnit(step);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);
    }

    static void initSliderInt(Slider slider, int min, int max, int step) {
        slider.setMin(min);
        slider.setMax(max);
        slider.setMajorTickUnit(step);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);
    }

    static void bind(TextField tf, Slider s, int dp) {
        tf.setOnAction(e->s.setValue(Double.parseDouble(tf.getText())));
        s.valueProperty().addListener((b,o,n)->tf.setText(String.format("%." + dp + "f", n)));
    }

    static void bindInt(TextField tf, Slider s) {
        tf.setOnAction(e->s.setValue(Integer.parseInt(tf.getText())));
        s.valueProperty().addListener((b,o,n)->tf.setText(String.format("%d", n.intValue())));
    }

    static TextFormatter<Double> doubleFormatter(double min, double max, double def) {
        Pattern validEditingState = Pattern.compile("(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) return c;
            else return null;
        };

        StringConverter<Double> d = new StringConverter<>() {
            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || ".".equals(s) || Double.parseDouble(s) < min || Double.parseDouble(s) > max) return def;
                else return Double.valueOf(s);
            }
            @Override
            public String toString(Double d) { return d.toString(); }
        };

        return new TextFormatter<>(d, def, filter);
    }

    static TextFormatter<Integer> intFormatter(int min, int max, int def) {
        Pattern validEditingStateInt = Pattern.compile("[\\d]*");

        UnaryOperator<TextFormatter.Change> filterInt = c -> {
            String text = c.getControlNewText();
            if (validEditingStateInt.matcher(text).matches()) return c;
            else return null;
        };

        StringConverter<Integer> i = new StringConverter<>() {
            @Override
            public Integer fromString(String s) {
                if (s.isEmpty() || Integer.parseInt(s) < min || Integer.parseInt(s) > max) return def;
                else return Integer.valueOf(s);
            }
            @Override
            public String toString(Integer d) { return d.toString(); }
        };

        return new TextFormatter<>(i, def, filterInt);
    }
}
