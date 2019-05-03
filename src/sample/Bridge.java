package sample;

import javafx.scene.control.Alert;


class Bridge {
    static Controller controller;
    static String extension = "";
    static String fileName = "";
    static void alertErrorMessage(String headerText, String contextText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(contextText);
        alert.setHeaderText(headerText);
        alert.show();
    }
}
