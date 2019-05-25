package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static javafx.scene.paint.Color.WHITE;

public class Controller {
    private String tool = "";
    private String fileName;
    private boolean toolFlag;

    @FXML
    protected MenuItem brush;
    @FXML
    protected MenuItem arc;
    @FXML
    protected MenuItem rectangle;
    @FXML
    protected MenuItem line;
    @FXML
    protected SplitMenuButton Tools;
    @FXML
    protected ColorPicker colorPicker;
    @FXML
    protected MenuBar menuBar;
    @FXML
    protected TabPane tabPane;
    @FXML
    protected MenuItem newFile;
    @FXML
    protected MenuItem openFile;
    @FXML
    protected MenuItem saveAs;
    @FXML
    protected MenuItem quit;
    @FXML
    protected MenuItem newTab;
    @FXML
    protected Label imageSize;
    @FXML
    protected MenuItem closeCurrentTab;
    @FXML
    protected Label CursorPositionLabel;
    @FXML
    protected Slider sliderSize;
    @FXML
    protected Label labelSliderSize;
    @FXML
    protected CheckBox checkFill;
    @FXML
    protected MenuItem btnNextTab;
    @FXML
    protected MenuItem btnPreviousTab;
    @FXML
    protected TextArea textFieldForTool;
    @FXML
    protected ChoiceBox<String> fontSize;
    @FXML
    protected ChoiceBox<String> Fonts;
    @FXML
    protected HBox sliderSizeHBox;
    @FXML
    protected HBox checkFillHBox;
    @FXML
    protected HBox textForToolHBox;
    @FXML
    protected HBox fontSizeHBox;
    @FXML
    protected HBox fontHBox;
    @FXML
    protected MenuItem btnPaste;
    @FXML
    protected MenuItem btnCopy;
    @FXML
    protected MenuItem btnUndo;
    @FXML
    protected Label scaleLabel;

    //***************************************************************************

    @FXML
    private void btnNewFile() {
        Stage dialogWindowStage = new Stage();
        InputStream stream = getClass().getResourceAsStream("dialogNewFileWindow.fxml");
        FXMLLoader loader = new FXMLLoader();
        try {
            Scene scene = new Scene(loader.load(stream));
            dialogWindowStage.setScene(scene);
            dialogWindowStage.setTitle("New File");
            dialogWindowStage.setResizable(false);
            dialogWindowStage.initModality(Modality.WINDOW_MODAL);
            dialogWindowStage.initOwner(menuBar.getScene().getWindow());
            dialogWindowStage.show();
        } catch (IOException e) {
            Bridge.alertErrorMessage("An error while creating file", "File was not created");
        }
    }

    @FXML
    protected void btnNewTab() {
        Tab tab = new Tab();
        int counter = tabPane.getTabs().size();
        counter++;
        tab.setText("Untitled Tab " + counter);
        tab.setContent(new BorderPane());
        tab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Bridge.controller.setTool(Bridge.controller.Tools.getText());
            }
        });
        tabCloseRequest(tab);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    @FXML
    private void btnOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a picture");
        FileChooser.ExtensionFilter filter = new FileChooser
                .ExtensionFilter("Pictures", "*.jpg", "*.bmp", "*.gif", "*.png");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        Image img = new Image(file.toURI().toString());
        ModifiedCanvas canvas = new ModifiedCanvas(img.getWidth(), img.getHeight());
        canvas.getGraphicsContext2D().drawImage(img, 0, 0);
        setToView(tabPane.getSelectionModel().getSelectedItem(), canvas);
        fileName = file.getName();
        tabPane.getSelectionModel().getSelectedItem().setText(fileName);

    }

    @FXML
    private void btnSaveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a directory");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pictures", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pictures", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pictures", "*.bmp"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pictures", "*.tiff"));
        fileChooser.setInitialFileName(getSelectedCanvas().getFilename());
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if (file != null) {
            saveImageToFile(file, getSelectedCanvas().getExtension());
        }
    }

    @FXML
    private void btnQuit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close the application?");
        boolean isFilesSaved = true;
        for (Tab tab : tabPane.getTabs()) {
            if (!getCanvasByTab(tab).isSaved()) {
                isFilesSaved = false;
            }
        }
        if (isFilesSaved) {
            alert.setHeaderText("Are you sure you want to exit the application?");
        } else {
            alert.setHeaderText("Are you sure you want to exit the application without saving files?");
            alert.setContentText("Some files are not saved");
        }
        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent()) {
            if (option.get() == ButtonType.OK) {
                System.exit(0);
            } else if (option.get() == ButtonType.CANCEL) {
                alert.close();
            }
        }
    }

    @FXML
    private void btnClear() {
        ModifiedCanvas canvas = Bridge.controller.getSelectedCanvas();
        if(canvas != null) {
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            graphicsContext.setFill(WHITE);
            graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    @FXML
    private void btnNextTab() {
        tabPane.getSelectionModel().selectNext();
    }


    @FXML
    private void btnPreviousTab() {
        if (!tabPane.getSelectionModel().isSelected(0)) {
            tabPane.getSelectionModel().selectPrevious();
        } else {
            tabPane.getSelectionModel().selectLast();
        }
    }

    @FXML
    private void btnCloseTab() {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        tabPane.getTabs().remove(tab);
    }

    @FXML
    private void toolLine() {
        Tools.setText("Line");
        setTool("Line");
    }

    @FXML
    private void toolOval() {
        Tools.setText("Oval");
        setTool("Oval");
    }

    @FXML
    private void toolBrush() {
        Tools.setText("Brush");
        setTool("Brush");
    }

    @FXML
    private void toolRectangle() {
        Tools.setText("Rectangle");
        setTool("Rectangle");
    }

    @FXML
    private void toolText() {
        Tools.setText("Text");
        setTool("Text");
    }

    @FXML
    private void toolQuadraticCurve() {
        Tools.setText("Quadratic curve");
        setTool("Quadratic curve");
    }

    @FXML
    private void btnPaste() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasImage()) {
            ModifiedCanvas canvas = Bridge.controller.getSelectedCanvas();
            if (canvas != null) {
                canvas.getGraphicsContext2D().drawImage(clipboard.getImage(), 0, 0, canvas.getWidth(), canvas.getHeight());
                canvas.getImageStack().push(clipboard.getImage());
            }
        }
    }

    @FXML
    private void btnCopy() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        ModifiedCanvas canvas = Bridge.controller.getSelectedCanvas();
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        WritableImage wImage = canvas.snapshot(new SnapshotParameters(), new WritableImage(width, height));
        content.putImage(wImage);
        clipboard.setContent(content);
    }

    @FXML
    private void btnUndo() {
        ModifiedCanvas canvas = getSelectedCanvas();
        if(canvas != null) {
            if (canvas.getImageStack().size() > 1) {
                canvas.getImageStack().pop();
                Image image = canvas.getImageStack().peek();
                canvas.getGraphicsContext2D().drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());
            }
        }
    }


    void setToView(Tab tab, ModifiedCanvas canvas) {
        ((BorderPane) tab.getContent()).setCenter(canvas);
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                CursorPositionLabel.setText("Cursor: " + (int)event.getX() + ":" + (int)event.getY());
            }
        });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                CursorPositionLabel.setText("Cursor: " + (int)event.getX() + ":" + (int)event.getY());
            }
        });
        if (toolFlag) {
            setTool(Tools.getText());
        }
        imageSize.setText("Size: " + (int) canvas.getWidth() + "*" + (int) canvas.getHeight() + " px");
        tab.setText(Bridge.controller.fileName);
    }

    private void saveImageToFile(File file, String extension) {
        try {
            ModifiedCanvas canvas = getSelectedCanvas();
            int width = (int) canvas.getWidth();
            int height = (int) canvas.getHeight();
            WritableImage wImage = canvas.snapshot(new SnapshotParameters(), new WritableImage(width, height));
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(wImage, null);
            BufferedImage bufferedImage1 = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            bufferedImage1.getGraphics().drawImage(bufferedImage, 0, 0, null);
            file.createNewFile();
            ImageIO.write(bufferedImage, extension, file);
        } catch (IOException e) {
            Bridge.alertErrorMessage("An error occurred while saving the file", "Undefined error");
        }
    }

    public Controller() {
        Bridge.controller = this;
    }

    void setTool(String tool) {
        unsetTool(this.tool);
        for (Tab tab : tabPane.getTabs()) {
            ModifiedCanvas canvas = (ModifiedCanvas) ((BorderPane) tab.getContent()).getCenter();
            if (canvas != null) {
                switch (tool) {
                    case "Brush":
                        this.tool = tool;
                        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, sample.Tools.brushDragged);
                        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, sample.Tools.brushPressed);
                        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, sample.Tools.brushReleased);
                        break;
                    case "Oval":
                        this.tool = tool;
                        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, sample.Tools.ovalReleased);
                        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, sample.Tools.ovalPressed);
                        break;
                    case "Line":
                        this.tool = tool;
                        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, sample.Tools.linePressed);
                        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, sample.Tools.lineReleased);
                        break;
                    case "Rectangle":
                        this.tool = tool;
                        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, sample.Tools.rectPressed);
                        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, sample.Tools.rectReleased);
                        break;
                    case "Text":
                        this.tool = tool;
                        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, sample.Tools.textClicked);
                        break;
                    case "Quadratic curve":
                        this.tool = tool;
                        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, sample.Tools.quadCurvePressed);
                        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, sample.Tools.quadCurveDragged);
                        break;
                }
            } else {
                toolFlag = true;
            }
        }
        hideElements(tool);
    }

    private void unsetTool(String tool) {
        for (Tab tab : tabPane.getTabs()) {
            ModifiedCanvas canvas = Bridge.controller.getSelectedCanvas();
            if (canvas != null) {
                switch (tool) {
                    case "Brush":
                        canvas.removeEventHandler(MouseEvent.MOUSE_PRESSED, sample.Tools.brushPressed);
                        canvas.removeEventHandler(MouseEvent.MOUSE_DRAGGED, sample.Tools.brushDragged);
                        canvas.removeEventHandler(MouseEvent.MOUSE_RELEASED, sample.Tools.brushReleased);
                        break;
                    case "Oval":
                        canvas.removeEventHandler(MouseEvent.MOUSE_PRESSED, sample.Tools.ovalPressed);
                        canvas.removeEventHandler(MouseEvent.MOUSE_RELEASED, sample.Tools.ovalReleased);
                        break;
                    case "Line":
                        canvas.removeEventHandler(MouseEvent.MOUSE_PRESSED, sample.Tools.linePressed);
                        canvas.removeEventHandler(MouseEvent.MOUSE_RELEASED, sample.Tools.lineReleased);
                        break;
                    case "Rectangle":
                        canvas.removeEventHandler(MouseEvent.MOUSE_PRESSED, sample.Tools.rectPressed);
                        canvas.removeEventHandler(MouseEvent.MOUSE_RELEASED, sample.Tools.rectReleased);
                        break;
                    case "Text":
                        canvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, sample.Tools.textClicked);
                        break;
                    case "Quadratic curve":
                        canvas.removeEventHandler(MouseEvent.MOUSE_PRESSED, sample.Tools.quadCurvePressed);
                        canvas.removeEventHandler(MouseEvent.MOUSE_DRAGGED, sample.Tools.quadCurveDragged);
                        break;
                }
            }
        }
        showElements();
    }

    void hideElements(String tool) {
        switch (tool) {
            case "Quadratic curve":
            case "Brush":
            case "Line":
                fontHBox.managedProperty().bind(fontHBox.visibleProperty());
                fontHBox.setVisible(false);
                fontSizeHBox.managedProperty().bind(fontSizeHBox.visibleProperty());
                fontSizeHBox.setVisible(false);
                textForToolHBox.managedProperty().bind(textForToolHBox.visibleProperty());
                textForToolHBox.setVisible(false);
                checkFillHBox.managedProperty().bind(checkFillHBox.visibleProperty());
                checkFillHBox.setVisible(false);
                break;
            case "Oval":
            case "Rectangle":
                fontHBox.managedProperty().bind(fontHBox.visibleProperty());
                fontHBox.setVisible(false);
                fontSizeHBox.managedProperty().bind(fontSizeHBox.visibleProperty());
                fontSizeHBox.setVisible(false);
                textForToolHBox.managedProperty().bind(textForToolHBox.visibleProperty());
                textForToolHBox.setVisible(false);
                break;
            case "Text":
                break;
            case "Everything":
                fontHBox.managedProperty().bind(fontHBox.visibleProperty());
                fontHBox.setVisible(false);
                fontSizeHBox.managedProperty().bind(fontSizeHBox.visibleProperty());
                fontSizeHBox.setVisible(false);
                checkFillHBox.managedProperty().bind(checkFillHBox.visibleProperty());
                checkFillHBox.setVisible(false);
                textForToolHBox.managedProperty().bind(textForToolHBox.visibleProperty());
                textForToolHBox.setVisible(false);
                sliderSizeHBox.managedProperty().bind(sliderSizeHBox.visibleProperty());
                sliderSizeHBox.setVisible(false);

        }
    }

    private void showElements() {
        fontHBox.managedProperty().bind(fontHBox.visibleProperty());
        fontHBox.setVisible(true);
        fontSizeHBox.managedProperty().bind(fontSizeHBox.visibleProperty());
        fontSizeHBox.setVisible(true);
        textForToolHBox.managedProperty().bind(textForToolHBox.visibleProperty());
        textForToolHBox.setVisible(true);
        checkFillHBox.managedProperty().bind(checkFillHBox.visibleProperty());
        checkFillHBox.setVisible(true);
        sliderSizeHBox.managedProperty().bind(sliderSizeHBox.visibleProperty());
        sliderSizeHBox.setVisible(true);
    }

    private ModifiedCanvas getSelectedCanvas() {
        return (ModifiedCanvas) ((BorderPane) tabPane.getSelectionModel().getSelectedItem().getContent()).getCenter();
    }

    void saveStep() {
        ModifiedCanvas canvas = getSelectedCanvas();
        WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(new SnapshotParameters(), image);
        canvas.getImageStack().push(image);
    }

    void tabCloseRequest(Tab tab) {
        tab.setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                ModifiedCanvas canvas = getCanvasByTab(tab);
                if(canvas != null) {
                    if (canvas.isSaved()) {
                        tabPane.getTabs().remove(tab);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Close the tab?");
                        alert.setHeaderText("Are you sure you want to close this tab without saving the file?");
                        alert.setContentText("File is not saved");
                        Optional<ButtonType> option = alert.showAndWait();
                        if (option.isPresent()) {
                            if (option.get() == ButtonType.OK) {
                                tabPane.getTabs().remove(tab);
                                alert.close();
                            } else if (option.get() == ButtonType.CANCEL) {
                                alert.close();
                            }
                        }
                    }
                }
            }
        });
    }

    private ModifiedCanvas getCanvasByTab(Tab tab) {
        return (ModifiedCanvas) (((BorderPane) (tab.getContent())).getCenter());
    }
}
