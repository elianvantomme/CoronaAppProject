package services.mixing_proxy;

import clients.visitor.Capsule;

import java.rmi.Remote;
import java.security.PublicKey;

public interface MixingProxyInterface extends Remote{

    void setRegistrarPublicKey(PublicKey publicKey) throws Exception;
    byte[] registerVisit(Capsule capsule) throws Exception;
}
