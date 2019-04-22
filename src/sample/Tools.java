package sample;


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Tools {
    static double lineWidth;
    private static boolean flag = false;
    private static double x;
    private static double y;
    static final EventHandler<MouseEvent> ovalPressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Bridge.graphicsContext.beginPath();
            Bridge.graphicsContext.moveTo(event.getX(), event.getY());
            x = event.getX();
            y = event.getY();
        }
    };
    static final EventHandler<MouseEvent> ovalReleased = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            double minX = Math.min(x, event.getX());
            double maxX = Math.max(x, event.getX());
            double minY = Math.min(y, event.getY());
            double maxY = Math.max(y, event.getY());
            setColor();
            setLineWidth();
            Bridge.graphicsContext.strokeOval(minX, minY, maxX - minX, maxY - minY);
        }
    };
    static final EventHandler<MouseEvent> brushDragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            setLineWidth();
            Bridge.graphicsContext.lineTo(event.getX(), event.getY());
            setColor();
            Bridge.graphicsContext.stroke();
        }
    };
    static final EventHandler<MouseEvent> brushPressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Bridge.graphicsContext.beginPath();
            setLineWidth();
            Bridge.graphicsContext.moveTo(event.getX(), event.getY());
            setColor();
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
            setColor();
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
    static final EventHandler<MouseEvent> rectPressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Bridge.graphicsContext.beginPath();
            Bridge.graphicsContext.moveTo(event.getX(), event.getY());
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
            if(flag) {
                double minX = Math.min(x, event.getX());
                double maxX = Math.max(x, event.getX());
                double minY = Math.min(y, event.getY());
                double maxY = Math.max(y, event.getY());
                setColor();
                Bridge.graphicsContext.strokeRect(minX,minY,maxX - minX, maxY - minY);
                flag = false;
            }
            Bridge.graphicsContext.save();
        }
    };

    static void setColor(){
        Bridge.graphicsContext.setStroke(Bridge.controller.colorPicker.getValue());
    }

    static void setLineWidth(){
        Bridge.graphicsContext.setLineWidth(Bridge.controller.sliderSize.getValue());
    }

//    static final List<EventHandler<MouseEvent>> brush = new ArrayList<>();
//    static final List<EventHandler<MouseEvent>> arc = new ArrayList<>();
//    static final List<EventHandler<MouseEvent>> line = new ArrayList<>();
//    static {
//        brush.add( brushDragged);
//        brush.add( brushPressed);
//        brush.add(brushReleased);
//        line.add( linePressed);
//        line.add(lineReleased);
//        arc.add(arcPressed);
//        arc.add(arcReleased);
//    }
    

    //public static final EventHandler<MouseEvent> rectangle;
}
