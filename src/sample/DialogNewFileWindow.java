package sample;

import javafx.fxml.FXML;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;

public class DialogNewFileWindow {
    @FXML
    protected TextField fileName;
    @FXML
    protected Button btnOK;
    @FXML
    protected Button btnCancel;
    @FXML
    protected MenuButton menuButton;

    //**************************************************************************

    public DialogNewFileWindow(){}

    protected void createNewFile() {

        File picFile = new File(fileName.getText() + menuButton.getText());
        if (picFile.exists()) {
            Bridge.alertErrorMessage("Cannot create file", "File already exists");
        } else {
            try {
                if (picFile.createNewFile()) {
                    if (Bridge.controller.tabPane.getSelectionModel().getSelectedItem() == null) {
                        Bridge.controller.btnNewTab();
                    }
                    Canvas canvas = new Canvas(1600, 900);
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    gc.setFill(Color.WHITE);
                    gc.fillRect(0,0,1600,900);
                    Bridge.controller.setToView(Bridge.controller.tabPane.getSelectionModel().getSelectedItem(), canvas);
                    Bridge.fileName = fileName.getText() + menuButton.getText();
                    Bridge.extension = Bridge.fileName.split("\\.")[1];
                    Bridge.controller.tabPane.getSelectionModel().getSelectedItem().setText(Bridge.fileName);
                }
            } catch (IOException e) {
                Bridge.alertErrorMessage("An error occurred while creating the file", "Permission denied");
            }
        }
    }

    @FXML
    protected void btnOK() {
        if (menuButton.getText().equals("Extension")) {
            Bridge.alertErrorMessage("An error occurred while creating the file", "Select the extension first");
        } else {
            if (fileName.getText().isEmpty()) {
                Bridge.alertErrorMessage("An error occurred while creating the file", "Enter the filename first");
            } else {
                createNewFile();
                exit();
            }
        }
    }

    @FXML
    protected void btnCancel() {
        exit();
    }

    private void exit() {
        Stage stage = (Stage) btnOK.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void setPngExtension() {
        menuButton.setText(".png");
    }

    @FXML
    protected void setJpgExtension() {
        menuButton.setText(".jpg");
    }

    @FXML
    protected void setBmpExtension() {
        menuButton.setText(".bmp");
    }
}
