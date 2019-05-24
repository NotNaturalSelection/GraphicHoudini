package sample;


import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;

class Tools {
    private static boolean flag = false;
    private static boolean flag1 = false;
    private static double x;
    private static double y;
    private static double x1;
    private static double y1;
    static final EventHandler<MouseEvent> ovalPressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            GraphicsContext graphicsContext = getGraphicsContext();
            graphicsContext.beginPath();
            graphicsContext.moveTo(event.getX(), event.getY());
            x = event.getX();
            y = event.getY();
        }
    };
    static final EventHandler<MouseEvent> ovalReleased = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            GraphicsContext graphicsContext = getGraphicsContext();
            double minX = Math.min(x, event.getX());
            double maxX = Math.max(x, event.getX());
            double minY = Math.min(y, event.getY());
            double maxY = Math.max(y, event.getY());
            setColor();
            setLineWidth();
            if (Bridge.controller.checkFill.isSelected()) {
                setFillColor();
                graphicsContext.fillOval(minX, minY, maxX - minX, maxY - minY);
            } else {
                graphicsContext.strokeOval(minX, minY, maxX - minX, maxY - minY);
            }
            Bridge.controller.saveStep();
        }
    };
    static final EventHandler<MouseEvent> brushPressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            GraphicsContext graphicsContext = getGraphicsContext();
            graphicsContext.save();
            graphicsContext.beginPath();
            setLineWidth();
            graphicsContext.moveTo(event.getX(), event.getY());
            setColor();
            flag = true;
        }
    };
    static final EventHandler<MouseEvent> brushDragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            GraphicsContext graphicsContext = getGraphicsContext();
            setLineWidth();
            graphicsContext.lineTo(event.getX(), event.getY());
            setColor();
            graphicsContext.stroke();
        }
    };
    static final EventHandler<MouseEvent> brushReleased = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            GraphicsContext graphicsContext = getGraphicsContext();
            if (flag) {
                graphicsContext.closePath();
                flag = false;
                Bridge.controller.saveStep();
            }
        }
    };
    static final EventHandler<MouseEvent> linePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            GraphicsContext graphicsContext = getGraphicsContext();
            graphicsContext.save();
            graphicsContext.beginPath();
            graphicsContext.moveTo(event.getX(), event.getY());
            setColor();
            x = event.getX();
            y = event.getY();
            graphicsContext.setLineWidth(Bridge.controller.sliderSize.getValue());
            flag = true;
        }
    };
    static final EventHandler<MouseEvent> lineReleased = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            GraphicsContext graphicsContext = getGraphicsContext();
            if (flag) {
                graphicsContext.setStroke(Bridge.controller.colorPicker.getValue());
                graphicsContext.strokeLine(x, y, event.getX(), event.getY());
                flag = false;
                Bridge.controller.saveStep();
            }
        }
    };
    static final EventHandler<MouseEvent> rectPressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            GraphicsContext graphicsContext = getGraphicsContext();
            graphicsContext.save();
            graphicsContext.beginPath();
            graphicsContext.moveTo(event.getX(), event.getY());
            setColor();
            x = event.getX();
            y = event.getY();
            setLineWidth();
            flag = true;
        }
    };
    static final EventHandler<MouseEvent> rectReleased = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            GraphicsContext graphicsContext = getGraphicsContext();
            if (flag) {
                double minX = Math.min(x, event.getX());
                double maxX = Math.max(x, event.getX());
                double minY = Math.min(y, event.getY());
                double maxY = Math.max(y, event.getY());
                setColor();
                if (Bridge.controller.checkFill.isSelected()) {
                    setFillColor();
                    graphicsContext.fillRect(minX, minY, maxX - minX, maxY - minY);
                } else {
                    graphicsContext.strokeRect(minX, minY, maxX - minX, maxY - minY);
                }
                flag = false;
                Bridge.controller.saveStep();
            }
        }
    };
    static final EventHandler<MouseEvent> textClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            try {
                GraphicsContext graphicsContext = getGraphicsContext();
                setColor();
                setFillColor();
                graphicsContext.setLineWidth(Bridge.controller.sliderSize.getValue());
                graphicsContext.setFont(Font.font(Bridge.controller.Fonts.getSelectionModel().getSelectedItem(), Double.parseDouble(Bridge.controller.fontSize.getValue())));
                if (Bridge.controller.checkFill.isSelected()) {
                    graphicsContext.fillText(Bridge.controller.textFieldForTool.getText(), event.getX(), event.getY());
                } else {
                    graphicsContext.strokeText(Bridge.controller.textFieldForTool.getText(), event.getX(), event.getY());
                }
                Bridge.controller.saveStep();
            } catch (NumberFormatException e) {
                Bridge.alertErrorMessage("Wrong font size", "Font size must be declared as numbers");
            }
        }
    };
    static final EventHandler<MouseEvent> quadCurvePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            GraphicsContext graphicsContext = getGraphicsContext();
            if (!flag1) {
                x = event.getX();
                y = event.getY();
                flag = true;
            } else {
                setColor();
                setLineWidth();
                graphicsContext.beginPath();
                graphicsContext.moveTo(x, y);
                graphicsContext.quadraticCurveTo(event.getX(), event.getY(), x1, y1);
                graphicsContext.stroke();
                graphicsContext.closePath();
                flag1 = false;
                flag = false;
                Bridge.controller.saveStep();
            }
        }
    };
    static final EventHandler<MouseEvent> quadCurveDragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (flag) {
                x1 = event.getX();
                y1 = event.getY();
                flag1 = true;
            }
        }
    };

    private static void setColor() {
        GraphicsContext graphicsContext = getGraphicsContext();
        graphicsContext.setStroke(Bridge.controller.colorPicker.getValue());
    }

    private static void setLineWidth() {
        GraphicsContext graphicsContext = getGraphicsContext();
        graphicsContext.setLineWidth(Bridge.controller.sliderSize.getValue());
    }

    private static void setFillColor() {
        GraphicsContext graphicsContext = getGraphicsContext();
        graphicsContext.setFill(Bridge.controller.colorPicker.getValue());
    }

    private static GraphicsContext getGraphicsContext() {
        return ((Canvas) ((BorderPane) (Bridge.controller.tabPane.getSelectionModel().getSelectedItem()).getContent()).getCenter()).getGraphicsContext2D();
    }

}
