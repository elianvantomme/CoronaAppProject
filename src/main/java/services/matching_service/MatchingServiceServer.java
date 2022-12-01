package services.matching_service;

import services.mixing_proxy.MixingProxyInterfaceImpl;
import services.mixing_proxy.MixingProxyServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MatchingServiceServer {
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
    }
}