package clients.doctor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DoctorApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DoctorApplication.class.getResource("doctorInterface-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        primaryStage.setTitle("Doctor Interface");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)   {
        launch(args);
    }
}
