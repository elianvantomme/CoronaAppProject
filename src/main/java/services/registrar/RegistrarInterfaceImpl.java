package services.registrar;

import clients.barowner.CateringFacility;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.LocalDate;
import java.util.*;

public class RegistrarInterfaceImpl extends UnicastRemoteObject implements RegistrarInterface {

    Map<CateringFacility, String> pseudonymList = new HashMap<>();
    KeyGenerator symKeyGenerator = KeyGenerator.getInstance("AES");
    SecretKey masterKey = symKeyGenerator.generateKey();

    public RegistrarInterfaceImpl() throws RemoteException, NoSuchAlgorithmException {}

    @Override
    public String enrollCF(CateringFacility cateringFacility) throws Exception {

        //check if CF allready enrolled

        if(pseudonymList.containsKey(cateringFacility)){
            System.out.println("in de if statement");
            return null;
        }

        //generate daily secret key from masterkey, cateringFacility and date
        LocalDate localDate = LocalDate.now();
        SecretKey dailySecretKey = symKeyGenerator.generateKey();

        pseudonymList.put(cateringFacility, "test");
        System.out.println(pseudonymList);

        System.out.println(pseudonymList.get(cateringFacility));


        return "null";
    }

}
