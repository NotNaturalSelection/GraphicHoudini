package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("sample.fxml").openStream());
        setAccelerators();
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void setAccelerators(){
        Bridge.controller.newFile.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        Bridge.controller.openFile.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        Bridge.controller.saveFile.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        Bridge.controller.quit.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));
        Bridge.controller.newTab.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));
        Bridge.controller.tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        Bridge.controller.closeCurrentTab.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));
        Bridge.controller.undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        Bridge.controller.sliderSize.setMin(1);
        Bridge.controller.sliderSize.setMax(10);
        Bridge.controller.sliderSize.setBlockIncrement(1);
        Bridge.controller.sliderSize.setShowTickMarks(true);
        Bridge.controller.sliderSize.setShowTickLabels(true);
        Bridge.controller.sliderSize.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Bridge.controller.labelSliderSize.setText("Size: " + new BigDecimal(newValue.doubleValue()).setScale(3, RoundingMode.UP).doubleValue());
            }
        });
    }
}
