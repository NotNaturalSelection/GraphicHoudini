package sample;


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Tools {
    static double lineWidth;
    static boolean flag = false;
    static double x;
    static double y;
    static final EventHandler<MouseEvent> arcPressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Bridge.graphicsContext.beginPath();
            Bridge.graphicsContext.moveTo(event.getX(), event.getY());
            x = event.getX();
            y = event.getY();
            System.out.println(x);
            System.out.println(y);
        }
    };
    static final EventHandler<MouseEvent> arcReleased = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Bridge.graphicsContext.arcTo(x, y, event.getX(), event.getY(), 300);
            Bridge.graphicsContext.setStroke(Bridge.controller.colorPicker.getValue());
            System.out.println(event.getX());
            System.out.println(event.getY());
            Bridge.graphicsContext.stroke();
        }
    };
    static final EventHandler<MouseEvent> brushDragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Bridge.graphicsContext.setLineWidth(Bridge.controller.sliderSize.getValue());
            Bridge.graphicsContext.lineTo(event.getX(), event.getY());
            Bridge.graphicsContext.setStroke(Bridge.controller.colorPicker.getValue());
            Bridge.graphicsContext.stroke();
        }
    };
    static final EventHandler<MouseEvent> brushPressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Bridge.graphicsContext.beginPath();
            Bridge.graphicsContext.setLineWidth(Bridge.controller.sliderSize.getValue());
            //System.out.println(Bridge.controller.sliderSize.getValue());
            Bridge.graphicsContext.moveTo(event.getX(), event.getY());
            Bridge.graphicsContext.setStroke(Bridge.controller.colorPicker.getValue());
            Bridge.graphicsContext.stroke();
            flag = true;
        }
    };
    static final EventHandler<MouseEvent> brushReleased = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(flag) {
                Bridge.graphicsContext.closePath();
                flag = false;
            }
        }
    };
    static final EventHandler<MouseEvent> linePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Bridge.graphicsContext.beginPath();
            Bridge.graphicsContext.moveTo(event.getX(), event.getY());
            Bridge.graphicsContext.setStroke(Bridge.controller.colorPicker.getValue());
            x = event.getX();
            y = event.getY();
            Bridge.graphicsContext.setLineWidth(Bridge.controller.sliderSize.getValue());
            flag = true;
        }
    };
    static final EventHandler<MouseEvent> lineReleased = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(flag) {
                Bridge.graphicsContext.setStroke(Bridge.controller.colorPicker.getValue());
                Bridge.graphicsContext.strokeLine(x,y,event.getX(), event.getY());
                flag = false;
            }

        }
    };

    //public static final EventHandler<MouseEvent> rectangle;
}
