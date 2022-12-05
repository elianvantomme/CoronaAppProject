package services.matching_service;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.mixing_proxy.MixingProxyServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MatchingServiceServer extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MatchingServiceServer.class.getResource("matchingServiceInterface-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Matching Service Interface");
        stage.setScene(scene);
        stage.show();
    }
    public void startServer(){
        try {
            Registry matchingServiceRegistry = LocateRegistry.createRegistry(4001);
            matchingServiceRegistry.rebind("MatchingService", new MatchingServiceInterfaceImp());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Matching service server system is ready");
    }
    public static void main(String[] args) {
        MatchingServiceServer matchingServiceServer = new MatchingServiceServer();
        matchingServiceServer.startServer();
        launch();
    }
}