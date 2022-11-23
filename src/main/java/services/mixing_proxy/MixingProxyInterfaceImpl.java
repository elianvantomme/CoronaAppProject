package services.mixing_proxy;

import clients.visitor.Capsule;
import services.registrar.Token;

import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MixingProxyInterfaceImpl extends UnicastRemoteObject implements MixingProxyInterface{

    private List<Token> usedTokens;
    private KeyPair mixingProxyKeyPair;
    private PublicKey registrarPublicKey;
    Signature sig;

    public MixingProxyInterfaceImpl() throws Exception {
        usedTokens = new ArrayList<>();
        mixingProxyKeyPair = generateKeyPair();
        sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(mixingProxyKeyPair.getPrivate());
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
                    System.out.println("capsule.getHashRandomNym() = " + capsule.getHashRandomNym());
                    sig.update(capsule.getHashRandomNym().getBytes());
                    byte[] signedHash = sig.sign();
                    return signedHash;
                }
            }
        }

        return null;

        //        Signature signature = Signature.getInstance("SHA256withRSA");
//        signature.initVerify(registrarPublicKey);
//        signature.update(capsule.getUserToken().getBytes());
//        if (!signature.verify(signature)))){
//            return "FALSE KEY";
//        }
//        return "OK";
    }


}
