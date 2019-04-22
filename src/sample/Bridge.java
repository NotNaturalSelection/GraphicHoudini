package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;

import java.util.ArrayList;


class Bridge {
    static Controller controller;
    static String extension;
    static String fileName = "";
    static Canvas canvas;
    static GraphicsContext graphicsContext;
    static void alertErrorMessage(String headerText, String contextText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(contextText);
        alert.setHeaderText(headerText);
        alert.show();
    }
}
