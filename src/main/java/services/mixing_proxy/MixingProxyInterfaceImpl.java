package services.mixing_proxy;

import clients.visitor.Capsule;
import services.registrar.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;

public class MixingProxyInterfaceImpl extends UnicastRemoteObject implements MixingProxyInterface{

    PublicKey registrarPublicKey;
    public MixingProxyInterfaceImpl() throws Exception {

    }

    @Override
    public void setRegistrarPublicKey(PublicKey publicKey) {
        registrarPublicKey = publicKey;
    }

    @Override
    public String registerVisit(Capsule capsule) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, ClassNotFoundException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        SignedObject signedToken = capsule.getSignedUserToken();
        Token token = (Token) signedToken.getObject();
        System.out.println("token = " + token);
        if(signedToken.verify(registrarPublicKey, signature)){
            return "OK";
        }else return "ERROR";


        //        Signature signature = Signature.getInstance("SHA256withRSA");
//        signature.initVerify(registrarPublicKey);
//        signature.update(capsule.getUserToken().getBytes());
//        if (!signature.verify(signature)))){
//            return "FALSE KEY";
//        }
//        return "OK";
    }


}
