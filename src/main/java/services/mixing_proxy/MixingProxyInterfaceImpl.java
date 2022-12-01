package services.mixing_proxy;

import clients.visitor.Capsule;
import services.matching_service.MatchingServiceInterface;
import services.registrar.Token;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MixingProxyInterfaceImpl extends UnicastRemoteObject implements MixingProxyInterface{

    private List<Token> usedTokens;
    private KeyPair mixingProxyKeyPair;
    private PublicKey registrarPublicKey;
    private Signature sig;
    private List<Capsule> capsuleList;
    private MatchingServiceInterface matchingServiceImpl;

    public MixingProxyInterfaceImpl() throws Exception {
        usedTokens = new ArrayList<>();
        capsuleList = new ArrayList<>();
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
        registrarPublicKey = publicKey;
    }

    @Override
    public byte[] registerVisit(Capsule capsule) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, ClassNotFoundException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        SignedObject signedToken = capsule.getSignedUserToken();
        Token token = (Token) signedToken.getObject();
        System.out.println("token = " + token);
        if(signedToken.verify(registrarPublicKey, signature)){
            if(!usedTokens.contains(token)){
                if(token.getLocalDate().isEqual(LocalDate.now())){
                    System.out.println("capsule.getHashRandomNym() = " + Arrays.toString(capsule.getHashRandomNym()));
                    sig.update(capsule.getHashRandomNym());
                    byte[] signedHash = sig.sign();
                    usedTokens.add(token);
                    capsuleList.add(capsule);
                    return signedHash;
                }
            }
        }
        return null;
    }

    public void flushCapsules() throws Exception{
        Collections.shuffle(capsuleList);
        matchingServiceImpl.receiveCapsules(capsuleList);
        capsuleList.clear();
    }

}
