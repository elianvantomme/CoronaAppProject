package services;
import services.matching_service.MatchingServiceInterfaceImp;
import services.mixing_proxy.MixingProxyInterfaceImpl;
import services.registrar.RegistrarInterfaceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;

public class ServerMain {
    public void startServer() throws NoSuchAlgorithmException {
        try {
            Registry registrarRegistry = LocateRegistry.createRegistry(4000);
            registrarRegistry.rebind("RegistrarService", new RegistrarInterfaceImpl());
            Registry matchingServiceRegistry = LocateRegistry.createRegistry(4001);
            matchingServiceRegistry.rebind("MatchingService", new MatchingServiceInterfaceImp());
            Registry mixingProxyRegistry = LocateRegistry.createRegistry(4002);
            mixingProxyRegistry.rebind("MixingProxyService", new MixingProxyInterfaceImpl());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("System is ready");
    }
    public static void main(String[] args) throws NoSuchAlgorithmException {
        ServerMain main = new ServerMain();
        main.startServer();
    }
}
