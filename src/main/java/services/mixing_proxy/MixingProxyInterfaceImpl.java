package services.mixing_proxy;

import clients.visitor.Capsule;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import services.matching_service.MatchingServiceInterface;
import services.registrar.Token;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MixingProxyInterfaceImpl extends UnicastRemoteObject implements MixingProxyInterface{
    /**********NORMAL VARIABLES*********/
    private List<Token> usedTokens;
    private KeyPair mixingProxyKeyPair;
    private Signature sig;
    static private MixingProxyContent mixingProxyContent;
    private MatchingServiceInterface matchingServiceImpl;

    /**********FXML VARIABLES*********/
    @FXML
    private TextArea mixingProxyQueue;
    @FXML
    private Button flushButton;

    public MixingProxyInterfaceImpl() throws Exception {
        usedTokens = new ArrayList<>();
        mixingProxyContent = new MixingProxyContent();
        mixingProxyKeyPair = generateKeyPair();
        sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(mixingProxyKeyPair.getPrivate());
        Registry matchingServiceRegistry = LocateRegistry.getRegistry("localhost",4001);
        matchingServiceImpl = (MatchingServiceInterface) matchingServiceRegistry.lookup("MatchingService");
    }

    public KeyPair generateKeyPair() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    @Override
    public void setRegistrarPublicKey(PublicKey publicKey) {
        mixingProxyContent.setRegistrarPublicKey(publicKey);
    }

    @Override
    public byte[] registerVisit(Capsule capsule) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        SignedObject signedToken = capsule.getSignedUserToken();
        Token token = (Token) signedToken.getObject();
        System.out.println("token = " + token);
        if(signedToken.verify(mixingProxyContent.getRegistrarPublicKey(), signature)){
            if(!usedTokens.contains(token)){
                if(token.getLocalDate().isEqual(LocalDate.now())){
                    System.out.println("capsule.getHashRandomNym() = " + Arrays.toString(capsule.getHashRandomNym()));
                    sig.update(capsule.getHashRandomNym());
                    byte[] signedHash = sig.sign();
                    usedTokens.add(token);
                    mixingProxyContent.addCapsule(capsule);
                    return signedHash;
                }
            }
        }
        return null;
    }

    public void refreshScreen(){
        mixingProxyQueue.setText(mixingProxyContent.printContent());
    }

    @FXML
    public void flushCapsules() throws Exception{
        List<Capsule> capsuleList = mixingProxyContent.getCapsuleList();
        Collections.shuffle(capsuleList);
        matchingServiceImpl.receiveCapsules(capsuleList);
        capsuleList.clear();
        refreshScreen();
    }
    @FXML
    public void initialize(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.plusMinutes(30);
        Timer timer = new Timer();
        TimerTask flushTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    flushCapsules();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(flushTask, Duration.between(now,nextRun).toMillis(), Duration.ofMinutes(30).toMillis());
        TimerTask refreshTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    refreshScreen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(refreshTask, Date.from(Instant.now()), Duration.ofSeconds(5).toMillis());
    }
}
