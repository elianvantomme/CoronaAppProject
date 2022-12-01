package services.matching_service;

import clients.visitor.Capsule;
import clients.visitor.LogEntry;

import java.security.PublicKey;
import java.security.Signature;
import java.security.SignedObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MatchingServiceInterfaceImp implements MatchingServiceInterface{

    private List<Capsule> capsuleList;
    private List<Capsule> uninformdInfectedCapsules;
    private List<Capsule> informdInfectedCapsules;
    private List<PublicKey> doctorPublicKeys;


    MatchingServiceInterfaceImp(){
        capsuleList = new ArrayList<>();
    }

    @Override
    public void addDoctorPublicKey(PublicKey publicKey) throws Exception {
        doctorPublicKeys.add(publicKey);
    }

    @Override
    public void receiveCapsules(List<Capsule> capsuleList) throws Exception {
        capsuleList.addAll(capsuleList);
    }

    public boolean checkSignature(SignedObject signedObject) throws Exception{
        Signature signature = Signature.getInstance("RSA");
        for(PublicKey publicKey : doctorPublicKeys){
            if(signedObject.verify(publicKey, signature)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void receiveLogs(SignedObject signedObject) throws Exception{

        if(checkSignature(signedObject)){
            System.out.println("is gesigned door een dokter");
            //TODO doe iets als het geen geldige signature is, maar dit zou nooit mogen gebeuren.
        }


        List<LogEntry> infectedLogs = (List<LogEntry>) signedObject.getObject();
        for(LogEntry log : infectedLogs){
            for (Capsule capsule : capsuleList) {
                if (log.getHash() == capsule.getHashRandomNym()){
                    if (containsTimeInterval(log.getEntryTime(),log.getLeaveTime(),log.getEntryTime(), log.getLeaveTime())){
                        uninformdInfectedCapsules.add(capsule);
                    }
                }
            }
        }
    }
    public boolean containsTimeInterval(LocalDateTime logStartDate, LocalDateTime logEndDate, LocalDateTime capStartDate, LocalDateTime capEndDate){
        for (LocalDateTime dateTime = capStartDate; dateTime.isBefore(capEndDate); dateTime = dateTime.plusMinutes(1)) {
            if (dateTime.isAfter(logStartDate) && dateTime.isBefore(logEndDate)){
                return true;
            }
        }
        return false;
    }
}
