package services.mixing_proxy;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MixingProxyServer {
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
    }
}
