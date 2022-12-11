package services.matching_service;

import clients.visitor.Capsule;
import clients.visitor.LogEntry;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import services.registrar.Token;

import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignedObject;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class MatchingServiceInterfaceImp extends UnicastRemoteObject implements MatchingServiceInterface{

    private MatchingServiceContent matchingServiceContent;

    @FXML
    private TextArea receivedCapsulesTextArea;
    @FXML
    private TextArea uninformedCapsulesTextArea;
    @FXML
    private TextArea informedCapsulesTextArea;
    @FXML
    private TextArea pseudonymsTextArea;

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

    @Override
    public void receiveInfectedSignedUsertokens(List<SignedObject> infectedSignedUsertokens) throws IOException, ClassNotFoundException {
        List<Capsule> uninformedCapsules = matchingServiceContent.getUninformdInfectedCapsules();
        for (int i = 0; i < uninformedCapsules.size(); i++) {
            Capsule uninformedCapsule = uninformedCapsules.get(i);
            Token uniformedToken = (Token) uninformedCapsule.getSignedUserToken().getObject();
            for (SignedObject signedObject: infectedSignedUsertokens) {
                Token infectedToken = (Token) signedObject.getObject();
                if (uniformedToken.equals(infectedToken)){
                    matchingServiceContent.addInformedCapsule(uninformedCapsule);
                    matchingServiceContent.removeUniformedCapsule(uninformedCapsule);
                }
            }
        }
    }
    public boolean checkSignature(SignedObject signedObject) throws Exception{
        Signature signature = Signature.getInstance("SHA256withRSA");
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
        }

        List<LogEntry> infectedLogs = (List<LogEntry>) signedObject.getObject();
        for(LogEntry log : infectedLogs){
            List<Capsule> tempList = matchingServiceContent.getCapsuleList();
            Iterator<Capsule> i = tempList.iterator();
            while(i.hasNext()){
                Capsule capsule = i.next();
                if (Arrays.equals(log.getHash(), capsule.getHashRandomNym())){
                    if (containsTimeInterval(log.getEntryTime(),log.getLeaveTime(),capsule.getStartInterval(), capsule.getEndInterval())){
                        matchingServiceContent.addUniformedCapsule(capsule);
                        i.remove();
                    }
                }
            }
            matchingServiceContent.setCapsuleList(tempList);
        }
    }

    @Override
    public void receivePseudonyms(List<String> pseudonyms) throws Exception{
        matchingServiceContent.addNewPseudonyms(pseudonyms);
        System.out.println(pseudonyms);
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
        pseudonymsTextArea.setText(matchingServiceContent.printPseudonyms());
    }
    @FXML
    public void initialize(){
        Timer timer = new Timer();
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
        timer.scheduleAtFixedRate(refreshTask, Date.from(Instant.now()), Duration.ofSeconds(3).toMillis());
    }

}
