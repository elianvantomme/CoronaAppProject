package services.registrar;

import clients.barowner.CateringFacility;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.rmi.Remote;
import java.security.SignedObject;
import java.util.List;

public interface RegistrarInterface extends Remote{
//    String loginCF(int phoneNumber) throws Exception;
    String loginCF(CateringFacility cateringFacility) throws Exception;
    List<SignedObject> loginVisitor(String phoneNumber) throws Exception;
    List<SignedObject> generateNewTokens(String phoneNumber) throws Exception;
}
