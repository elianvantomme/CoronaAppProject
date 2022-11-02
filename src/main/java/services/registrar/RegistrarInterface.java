package services.registrar;

import java.rmi.Remote;
import java.util.Set;

public interface RegistrarInterface extends Remote{
    String loginCF(int phoneNumber) throws Exception;
    Set<String> loginVisitor(String phoneNumber) throws Exception;
}
