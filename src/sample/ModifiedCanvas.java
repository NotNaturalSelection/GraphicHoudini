package sample;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.Stack;

public class ModifiedCanvas extends Canvas {
    private Stack<Image> imageStack;
    private boolean saveFlag;
    private String filename = "";

    public void setSaveFlag(boolean saveFlag) {
        this.saveFlag = saveFlag;
    }

    private String extension = "";

    public Stack<Image> getImageStack() {
        return imageStack;
    }

    ModifiedCanvas(double x, double y){
        super(x,y);
        saveFlag = false;
        imageStack = new Stack<>();
        WritableImage image = new WritableImage((int)this.getWidth(), (int)this.getHeight());
        imageStack.push(this.snapshot(new SnapshotParameters(), image));
        this.setOnScroll((event) -> {
        double scaleX = this.getScaleX();
        double scaleY = this.getScaleY();
        if (scaleX <= 0.8 && scaleY <= 0.8) {
            scaleX = 0.8;
            scaleY = 0.8;
        }
        if (scaleX >= 2 && scaleY >= 2) {
            scaleX = 2;
            scaleY = 2;
        }
        this.setScaleX(scaleX + event.getDeltaY() / 800);
        this.setScaleY(scaleY + event.getDeltaY() / 800);
        Bridge.controller.scaleLabel.setText("Scale:" + (int)(100*scaleX) + "%");
    });
    }

    public boolean isSaved() {
        return saveFlag;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
