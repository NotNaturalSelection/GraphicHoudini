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
    @FXML
    protected TextField fileWidth;
    @FXML
    protected TextField fileHeight;
    //**************************************************************************

    public DialogNewFileWindow() {
    }

    protected void createNewFile() {
        int width = Integer.parseInt(fileWidth.getText());
        int height = Integer.parseInt(fileHeight.getText());

        File picFile = new File(fileName.getText() + menuButton.getText());
        if (picFile.exists()) {
            Bridge.alertErrorMessage("Cannot create file", "File already exists");
        } else {
            try {
                if (picFile.createNewFile()) {
                    if (Bridge.controller.tabPane.getSelectionModel().getSelectedItem() == null) {
                        Bridge.controller.btnNewTab();
                    }
                    ModifiedCanvas canvas = new ModifiedCanvas(width, height);
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    gc.setFill(Color.WHITE);
                    gc.fillRect(0, 0, width, height);
                    Bridge.controller.setToView(Bridge.controller.tabPane.getSelectionModel().getSelectedItem(), canvas);
                    canvas.setFilename(fileName.getText());
                    canvas.setExtension(menuButton.getText().replace(".", ""));
                    Bridge.controller.tabPane.getSelectionModel().getSelectedItem().setText(canvas.getFilename() + canvas.getExtension());
                    Bridge.controller.setTool(Bridge.controller.Tools.getText());
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
                try {
                    createNewFile();
                    exit();
                } catch (NumberFormatException e) {
                    Bridge.alertErrorMessage("Wrong resolution format", "Resolution must be declared as numbers");
                }
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
