package services.mixing_proxy;

import clients.visitor.Capsule;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Map;

public interface MixingProxyInterface extends Remote{
    void setRegistrarPublicKey(PublicKey publicKey) throws Exception;
    String registerVisit(Capsule capsule) throws Exception;
}
