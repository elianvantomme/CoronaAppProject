package services.registrar;

import services.mixing_proxy.MixingProxyInterfaceImpl;
import services.mixing_proxy.MixingProxyServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;

public class RegistrarServer {

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

    }
}
