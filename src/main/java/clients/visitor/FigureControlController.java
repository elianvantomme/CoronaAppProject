package clients.visitor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class FigureControlController {
    @FXML
    private Canvas FigureDisplay;

    private String figureHash;

    public void setFigureHash(String figureHash) {
        this.figureHash = figureHash;
    }

    @FXML
    public void initialize(){

        GraphicsContext context = FigureDisplay.getGraphicsContext2D();
        //String colorValue = figureHash.substring(0,6);
        //context.setFill(Color.web(String.format("%x", new BigInteger(1,colorValue.getBytes()))));
        context.setFill(Color.rgb(50,255,30,0.50));
        context.fillRect(20,20,180,180);
    }
}
