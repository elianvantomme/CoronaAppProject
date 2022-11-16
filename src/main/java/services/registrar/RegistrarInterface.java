package services.registrar;

import clients.barowner.CateringFacility;

import java.rmi.Remote;
import java.util.Set;

public interface RegistrarInterface extends Remote{
    String enrollCF(CateringFacility cateringFacility) throws Exception;
}
