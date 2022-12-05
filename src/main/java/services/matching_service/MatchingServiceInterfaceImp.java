package services.matching_service;

import clients.visitor.Capsule;
import clients.visitor.LogEntry;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignedObject;
import java.time.LocalDateTime;
import java.util.List;

public class MatchingServiceInterfaceImp extends UnicastRemoteObject implements MatchingServiceInterface{

    private MatchingServiceContent matchingServiceContent;

    @FXML
    private TextArea receivedCapsulesTextArea;
    @FXML
    private TextArea uninformedCapsulesTextArea;
    @FXML
    private TextArea informedCapsulesTextArea;

    public MatchingServiceInterfaceImp() throws Exception{
        matchingServiceContent = new MatchingServiceContent();
    }

    @Override
    public void addDoctorPublicKey(PublicKey publicKey) throws Exception {
        matchingServiceContent.addDoctorKey(publicKey);
    }

    @Override
    public List<Capsule> getInfectedCapsules() throws Exception {
        return matchingServiceContent.getUninformdInfectedCapsules();
    }

    @Override
    public void receiveCapsules(List<Capsule> capsuleList) throws Exception {
        matchingServiceContent.addCapsulesToList(capsuleList);
    }

    public boolean checkSignature(SignedObject signedObject) throws Exception{
        Signature signature = Signature.getInstance("RSA");
        for(PublicKey publicKey : matchingServiceContent.getDoctorPublicKeys()){
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
            for (Capsule capsule : matchingServiceContent.getCapsuleList()) {
                if (log.getHash() == capsule.getHashRandomNym()){
                    if (containsTimeInterval(log.getEntryTime(),log.getLeaveTime(),log.getEntryTime(), log.getLeaveTime())){
                        matchingServiceContent.addUniformedCapsule(capsule);
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
    @FXML
    public void refreshScreen(){
        receivedCapsulesTextArea.setText(matchingServiceContent.printCapsuleList());
        uninformedCapsulesTextArea.setText(matchingServiceContent.printUninformedCapsuleList());
        informedCapsulesTextArea.setText(matchingServiceContent.printInformedCapsuleList());
    }

}
