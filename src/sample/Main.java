package sample;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("sample.fxml").openStream());
        setAccelerators();
        primaryStage.setTitle("Graphic Houdini");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void setAccelerators() {
        Bridge.controller.newFile.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        Bridge.controller.openFile.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        Bridge.controller.saveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        Bridge.controller.quit.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));
        Bridge.controller.newTab.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));
        Bridge.controller.tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        Bridge.controller.closeCurrentTab.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));
        Bridge.controller.btnNextTab.setAccelerator(new KeyCodeCombination(KeyCode.TAB, KeyCombination.CONTROL_DOWN));
        Bridge.controller.btnPreviousTab.setAccelerator(new KeyCodeCombination(KeyCode.TAB, KeyCombination.SHIFT_DOWN));
        Bridge.controller.btnPaste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
        Bridge.controller.btnCopy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        Bridge.controller.btnUndo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
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
        Bridge.controller.tabPane.getSelectionModel().getSelectedItem().setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Bridge.controller.setTool(Bridge.controller.Tools.getText());
            }
        });
        for (Tab tab:Bridge.controller.tabPane.getTabs()) {
            Bridge.controller.tabCloseRequest(tab);
        }
        ObservableList<String> fonts = FXCollections.observableArrayList(Font.getFamilies());
        ObservableList<String> fontSizes = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7",
                "8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "32", "36", "40", "44", "48",
                "52", "56", "60", "64", "68", "72", "78", "84", "90", "96", "108", "120", "138", "156", "174", "192", "200",
                "225", "250", "275", "300");
        Bridge.controller.fontSize.setItems(fontSizes);
        Bridge.controller.fontSize.setValue("12");
        Bridge.controller.Fonts.setItems(fonts);
        Bridge.controller.Fonts.setValue("Calibri");
        Bridge.controller.hideElements("Everything");
    }
}
