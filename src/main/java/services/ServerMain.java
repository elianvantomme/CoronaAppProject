package services;
import services.registrar.RegistrarInterfaceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public void startServer(){
        try {
            Registry registrarRegistry = LocateRegistry.createRegistry(4000);
            registrarRegistry.rebind("RegistrarService", new RegistrarInterfaceImpl());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("System is ready");
    }
    public static void main(String[] args) {
        ServerMain main = new ServerMain();
        main.startServer();
    }
}
