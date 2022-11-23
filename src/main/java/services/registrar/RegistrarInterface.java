package services.registrar;

import clients.barowner.CateringFacility;

import java.rmi.Remote;
import java.util.Set;

public interface RegistrarInterface extends Remote{
//    String loginCF(int phoneNumber) throws Exception;
    String loginCF(CateringFacility cateringFacility) throws Exception;
    Set<String> loginVisitor(String phoneNumber) throws Exception;
}
