package services.mixing_proxy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.registrar.RegistrarServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MixingProxyServer extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MixingProxyServer.class.getResource("mixingProxyInterface-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 422, 400);
        stage.setTitle("Mixing Proxy Interface");
        stage.setScene(scene);
        stage.show();
    }

    public void startServer(){
        try {
            Registry mixingProxyRegistry = LocateRegistry.createRegistry(4002);
            mixingProxyRegistry.rebind("MixingProxyService", new MixingProxyInterfaceImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("mixing proxy System is ready");
    }
    public static void main(String[] args) {
        MixingProxyServer mixingProxyServer = new MixingProxyServer();
        mixingProxyServer.startServer();
        launch();
    }


}
