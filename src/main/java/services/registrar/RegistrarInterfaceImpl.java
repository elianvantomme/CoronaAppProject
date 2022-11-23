package services.registrar;

import clients.barowner.CateringFacility;
import services.mixing_proxy.MixingProxyInterface;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.LocalDate;
import java.util.*;

public class RegistrarInterfaceImpl extends UnicastRemoteObject implements RegistrarInterface {
    private KeyGenerator keyGenerator;
    private SecretKey masterSecretKey;
    private Set<String> registeredPhoneNumbers;
    private Map<String, List<Token>> oldTokensMap;
    private Map<String, List<Token>> validTokensMap;
    private MixingProxyInterface mixingProxyInterface;
    private KeyPair tokensKeyPair;

    public RegistrarInterfaceImpl () throws Exception {
         keyGenerator = KeyGenerator.getInstance("AES");
         masterSecretKey = keyGenerator.generateKey();
         registeredPhoneNumbers = new HashSet<>();
         oldTokensMap = new HashMap<>();
         validTokensMap = new HashMap<>();
         mixingProxyInterface = (MixingProxyInterface) LocateRegistry.
                 getRegistry("localhost", 4002).
                 lookup("MixingProxyService");
        tokensKeyPair = generateKeyPairForSigningTokens();
    }

    public KeyPair generateKeyPairForSigningTokens() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //send this public key to the mixingproxy
        mixingProxyInterface.setRegistrarPublicKey(keyPair.getPublic());
        return keyPair;
    }

    @Override
    public String loginCF(CateringFacility cateringFacility) throws NoSuchAlgorithmException, InvalidKeySpecException {
        LocalDate localDate = LocalDate.now();

        //Check if the CF already is registered
//        if (!secretKeyMap.containsKey(phoneNumber)){
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//            SecretKey masterSecretKey = keyGenerator.generateKey();
//            secretKeyMap.put(phoneNumber, masterSecretKey);
//        }

        //Generate the Daily Secret key from: master secret key, CF info, day
        String KDFinput = cateringFacility.toString() + masterSecretKey.toString() + localDate;
        SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
        KeySpec keySpecs = new SecretKeySpec(KDFinput.getBytes(StandardCharsets.UTF_8), "AES");
        SecretKey dailySecretKey = kf.generateSecret(keySpecs);


        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] byteArray = dailySecretKey.toString().concat(cateringFacility.toString()).concat(localDate.toString()).getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(md.digest(byteArray));
    }

    @Override
    public List<SignedObject> loginVisitor(String phoneNumber) throws Exception {
        if(!registeredPhoneNumbers.add(phoneNumber)){ // only add new users
            return generateNewTokens(phoneNumber);
        }
        return new ArrayList<>();
    }

    public List<SignedObject> generateNewTokens(String phoneNumber) throws Exception {
        LocalDate date = LocalDate.now();
        List<Token> newUserTokens = new ArrayList<>();
        List<SignedObject> newUserSignedTokens = new ArrayList<>();

        if(oldTokensMap.containsKey(phoneNumber)){
            oldTokensMap.get(phoneNumber).addAll(validTokensMap.get(phoneNumber));
        }

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(tokensKeyPair.getPrivate());
        //Create 48 new signed tokens for a certain phone number
        for (int i = 0; i < 48; i++) {
//            byte[] tokenString = (String.valueOf(Math.random()).concat(date.toString())).getBytes(StandardCharsets.UTF_8);
//            signature.update(tokenString);
//            String signedToken = Base64.getEncoder().encodeToString(tokenString);
//            newUserTokens.add(signedToken);
            Token token = new Token();
            SignedObject signedToken = new SignedObject(token, tokensKeyPair.getPrivate(), signature);
            newUserSignedTokens.add(signedToken);
            newUserTokens.add(token);
        }
        validTokensMap.put(phoneNumber,newUserTokens);
        return newUserSignedTokens;
    }



}
