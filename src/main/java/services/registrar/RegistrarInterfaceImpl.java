package services.registrar;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RegistrarInterfaceImpl extends UnicastRemoteObject implements RegistrarInterface {

    public RegistrarInterfaceImpl() throws RemoteException{}
}
