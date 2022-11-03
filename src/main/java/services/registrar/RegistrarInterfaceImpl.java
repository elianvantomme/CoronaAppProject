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
    Map<Integer, SecretKey> secretKeyMap = new HashMap<>();
    Set<String> registeredPhoneNumbers = new HashSet<>();
    Map<String, String> oldTokensMap = new HashMap<>();
    Map<String, String> validTokensMap = new HashMap<>();

    Map<CateringFacility, String> pseudonymList = new HashMap<>();
    KeyGenerator symKeyGenerator = KeyGenerator.getInstance("AES");
    SecretKey masterKey = symKeyGenerator.generateKey();

    public RegistrarInterfaceImpl() throws RemoteException, NoSuchAlgorithmException {}

//    @Override
//    public String loginCF(int phoneNumber) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        LocalDate localDate = LocalDate.now();
//
//        //Check if the CF already is registered
//        if (!secretKeyMap.containsKey(phoneNumber)){
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//            SecretKey masterSecretKey = keyGenerator.generateKey();
//            secretKeyMap.put(phoneNumber, masterSecretKey);
//        }
//
//        //Generate the Daily Secret key from: master secret key, CF info, day
//        SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
//        KeySpec keySpecs = new SecretKeySpec(secretKeyMap.get(phoneNumber).toString().getBytes(StandardCharsets.UTF_8), "AES");
//        SecretKey dailySecretKey = kf.generateSecret(keySpecs);
//
//
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[] byteArray = dailySecretKey.toString().concat(String.valueOf(phoneNumber)).concat(localDate.toString()).getBytes(StandardCharsets.UTF_8);
//        return Base64.getEncoder().encodeToString(md.digest(byteArray));
//    }

    @Override
    public void test(CateringFacility cateringFacility) throws Exception {

    }

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


        return "null";
    }

    @Override
    public Set<String> loginVisitor(String phoneNumber) throws Exception {
        if(!registeredPhoneNumbers.add(phoneNumber)){ // only add new users
            return generateNewTokens(phoneNumber);
        }
        return new HashSet<>();
    }

    public Set<String> generateNewTokens(String phoneNumber) throws Exception {
        LocalDate date = LocalDate.now();
        Set<String> newUserTokens = new HashSet<>();

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        //Create 48 new signed tokens for a certain phone number
        for (int i = 0; i < 48; i++) {
            byte[] tokenString = (String.valueOf(Math.random()).concat(date.toString())).getBytes(StandardCharsets.UTF_8);
            signature.update(tokenString);
            String signedToken = Base64.getEncoder().encodeToString(tokenString);
            validTokensMap.put(signedToken, phoneNumber);
            newUserTokens.add(signedToken);
        }
        return newUserTokens;
    }
}
