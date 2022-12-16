package services.registrar;

import clients.barowner.CateringFacility;
import services.matching_service.MatchingServiceInterface;
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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class RegistrarInterfaceImpl extends UnicastRemoteObject implements RegistrarInterface {
    /**********NORMAL VARIABLES*********/
    private KeyGenerator keyGenerator;
    private SecretKey masterSecretKey;
    private Set<String> registeredPhoneNumbers;
    private MixingProxyInterface mixingProxyInterface;
    private MatchingServiceInterface matchingServiceInterface;
    private KeyPair tokensKeyPair;
    private static RegistrarContent registrarContent;

    public RegistrarInterfaceImpl () throws Exception {
        keyGenerator = KeyGenerator.getInstance("AES");
        masterSecretKey = keyGenerator.generateKey();
        registeredPhoneNumbers = new HashSet<>();

        mixingProxyInterface = (MixingProxyInterface) LocateRegistry.
                 getRegistry("localhost", 4002).
                 lookup("MixingProxyService");
        matchingServiceInterface = (MatchingServiceInterface) LocateRegistry
                .getRegistry("localhost",4001)
                .lookup("MatchingService");
        tokensKeyPair = generateKeyPairForSigningTokens();
        registrarContent = new RegistrarContent();
        sendPseudonyms();
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
        //Generate the Daily Secret key from: master secret key, CF info, day
        String KDFinput = cateringFacility.getAddress() + masterSecretKey.toString() + localDate;
        SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
        KeySpec keySpecs = new SecretKeySpec(KDFinput.getBytes(StandardCharsets.UTF_8), "AES");
        SecretKey dailySecretKey = kf.generateSecret(keySpecs);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] byteArray = dailySecretKey.toString().concat(cateringFacility.toString()).concat(localDate.toString()).getBytes(StandardCharsets.UTF_8);
        String pseudonym = Base64.getEncoder().encodeToString(md.digest(byteArray));
        cateringFacility.setPseudonym(pseudonym);
        if (!registrarContent.containsCF(cateringFacility)){
            registrarContent.addCateringFacility(cateringFacility);
        }
        refreshScreen();
        return pseudonym;
    }

    @Override
    public List<SignedObject> loginVisitor(String phoneNumber) throws Exception {
        if(registeredPhoneNumbers.add(phoneNumber)){ // only add new users
            registrarContent.addVisitor(phoneNumber);
        }
        refreshScreen();
        return generateNewTokens(phoneNumber);
    }
    public void sendPseudonyms() throws Exception {
        List<String> pseudonyms = new ArrayList<>();
        Timer timer = new Timer();
        LocalDateTime todayMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).plusDays(1);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    pseudonyms.clear();
                    for (CateringFacility cateringFacility: RegistrarContent.getCateringFacilityList()) {
                        pseudonyms.add(cateringFacility.getPseudonym());

                    }
                    refreshScreen();
                    matchingServiceInterface.receivePseudonyms(pseudonyms);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(task, Duration.between(LocalDateTime.now(), todayMidnight).toMillis() , Duration.ofDays(1).toMillis());
    }
    @Override
    public List<SignedObject> generateNewTokens(String phoneNumber) throws Exception {
        LocalDate date = LocalDate.now();
        List<Token> newUserTokens = new ArrayList<>();
        List<SignedObject> newUserSignedTokens = new ArrayList<>();
        Map<String, List<Token>> oldTokensMap = registrarContent.getOldTokensMap();
        if(oldTokensMap.containsKey(phoneNumber)){
            oldTokensMap.get(phoneNumber).addAll(registrarContent.getValidTokens(phoneNumber));
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
        registrarContent.addValidTokens(phoneNumber,newUserTokens);
        return newUserSignedTokens;
    }
    public void refreshScreen(){
        System.out.println("------ CONTENT REGISTRAR ------");
        System.out.println(registrarContent.printVisitorList());
        System.out.println(registrarContent.printCateringFacilityList());
        System.out.println("""





                """);
    }
}
