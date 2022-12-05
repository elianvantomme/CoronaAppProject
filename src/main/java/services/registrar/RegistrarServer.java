package services.registrar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RegistrarServer {

//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(RegistrarServer.class.getResource("registrarInterface-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
//        stage.setTitle("Registrar Interface");
//        stage.setScene(scene);
//        stage.show();
//    }

    public void startServer(RegistrarInterfaceImpl registrarInterface){
        try {
            Registry registrarRegistry = LocateRegistry.createRegistry(4000);
            registrarRegistry.rebind("RegistrarService", registrarInterface);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("registrar service System is ready");
    }

    public static void main(String[] args) throws Exception {
        RegistrarServer registrarServer = new RegistrarServer();
        RegistrarInterfaceImpl registrarInterface = new RegistrarInterfaceImpl();
        registrarServer.startServer(registrarInterface);
//        launch();
    }
}
