package services.registrar;

import clients.barowner.CateringFacility;

import java.rmi.Remote;
import java.security.SignedObject;
import java.util.List;

public interface RegistrarInterface extends Remote{
    String loginCF(CateringFacility cateringFacility) throws Exception;
    List<SignedObject> loginVisitor(String phoneNumber) throws Exception;
    List<SignedObject> generateNewTokens(String phoneNumber) throws Exception;
}
