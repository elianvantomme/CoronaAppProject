package services.matching_service;

import clients.visitor.Capsule;
import clients.visitor.LogEntry;

import java.rmi.Remote;
import java.security.PublicKey;
import java.security.SignedObject;
import java.util.List;

public interface MatchingServiceInterface extends Remote {

    void receiveCapsules(List<Capsule> capsuleList) throws Exception;
    void receiveLogs(SignedObject signedObject) throws Exception;
    void addDoctorPublicKey(PublicKey publicKey) throws Exception;
    List<Capsule> getInfectedCapsules() throws Exception;

}
