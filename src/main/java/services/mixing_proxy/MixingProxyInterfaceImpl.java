package services.mixing_proxy;

import clients.visitor.Capsule;

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
    public String registerVisit(Capsule capsule) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return "Error";


        //        Signature signature = Signature.getInstance("SHA256withRSA");
//        signature.initVerify(registrarPublicKey);
//        signature.update(capsule.getUserToken().getBytes());
//        if (!signature.verify(signature)))){
//            return "FALSE KEY";
//        }
//        return "OK";
    }


}
