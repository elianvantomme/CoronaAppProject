package clients.visitor;

import clients.doctor.DoctorClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import services.matching_service.MatchingServiceInterface;
import services.matching_service.MatchingServiceInterfaceImp;
import services.mixing_proxy.MixingProxyInterface;
import services.registrar.RegistrarContent;
import services.registrar.RegistrarInterface;
import services.registrar.RegistrarInterfaceImpl;
import services.registrar.Token;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.SignedObject;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class RegisterVisitController {
    //TODO gaan verwijderen van de visistedCFs na een bepaald aantal dagen
//    Map<LocalDateTime, String[]> visitedCFs = new HashMap<>();
    private ArrayList<String[]> visitedCFs;
    private ArrayList<Capsule> capsules;
    private DoctorClient doctor;
    private RegistrarInterface registrarImpl;
    private MixingProxyInterface mixingProxyImpl;
    private MatchingServiceInterface matchingServiceImpl;
    private List<SignedObject> validTokens;
    private List<SignedObject> usedTokens;
    private String phoneNumber;
    private byte[] signedHash;
    private ArrayList<LogEntry> logs;
    private Timer timer;
    private boolean leavingCateringFacility;

    @FXML
    private TextField qrDataStringField;
    @FXML
    private Button SubmitButtonDataString;
    @FXML
    private Canvas FigureDisplay;
    @FXML
    private Button leaveCFButton;
    @FXML
    private Button medicButton;
    @FXML
    private Button infectedButton;
    @FXML
    private Pane infectionAlertPane;

    public RegisterVisitController() throws Exception {
        this.visitedCFs = new ArrayList<>();
        this.capsules = new ArrayList<>();
        this.doctor = new DoctorClient();
        this.validTokens = new ArrayList<>();
        this.usedTokens = new ArrayList<>();
        this.logs = new ArrayList<>();
        this.timer = new Timer();
        leavingCateringFacility = false;
    }

    public void setValidTokens(List<SignedObject> validTokens) {
        this.validTokens = validTokens;
    }

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    @FXML
    private void onClickSubmitDataString() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();
        String[] parts = new String[3];
        parts[0]= dateTime.toString();
        parts[1]= "null";
        parts[2]= qrDataStringField.getText();
        visitedCFs.add(parts);
        String[] qrDataString = qrDataStringField.getText().split("@");

        String randomDouble = qrDataString[0];
        String cateringFacilityInfoString = qrDataString[1];
        String pseudonymHash = qrDataString[2];

        final LocalDateTime[] beginTimeInterval = {dateTime
                .truncatedTo(ChronoUnit.HOURS)
                .plusMinutes(30 * (dateTime.getMinute() / 30))};
        final LocalDateTime[] endTimeInterval = {beginTimeInterval[0].plusMinutes(30)};
        if (!validTokens.isEmpty()){
            sendCapsule(randomDouble, cateringFacilityInfoString, pseudonymHash, beginTimeInterval[0], endTimeInterval[0]);
            if(signedHash != null){
                qrDataStringField.setVisible(false);
                SubmitButtonDataString.setVisible(false);
                medicButton.setVisible(false);
                infectedButton.setVisible(false);
                leaveCFButton.setVisible(true);
                FigureDisplay.setVisible(true);
                GraphicsContext context = FigureDisplay.getGraphicsContext2D();
                String colorValueString = Base64.getEncoder().encodeToString(signedHash).substring(0,3);
                StringBuffer sb = new StringBuffer();
                char ch[] =  colorValueString.toCharArray();
                for (char c : ch) {
                    sb.append(Integer.toHexString(c));
                }
                context.setFill(Color.web(sb.toString()));
                context.fillRect(20,20,180,180);

                //Send capsule
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime nextRun = now.plusMinutes(30);

                Timer capsuleTimer = new Timer();
                TimerTask flushTask = new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            if (!leavingCateringFacility) {
                                sendCapsule(randomDouble, cateringFacilityInfoString, pseudonymHash, beginTimeInterval[0], endTimeInterval[0]);
                                beginTimeInterval[0] = endTimeInterval[0];
                                endTimeInterval[0] = endTimeInterval[0].plusMinutes(30);
                            }
                            else{
                                leavingCateringFacility = false;
                                capsuleTimer.cancel();
                                capsuleTimer.purge();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                capsuleTimer.scheduleAtFixedRate(flushTask, Duration.between(now,nextRun).toMillis(), Duration.ofMinutes(30).toMillis());

            } else {
                //TODO: genereer misschien een scherm waarop staat dat je een foute code hebt gestuurd
            }
        }
    }

    private void sendCapsule(String randomDouble, String cateringFacilityInfoString, String pseudonymHash, LocalDateTime beginTimeInterval, LocalDateTime endTimeInterval) throws Exception {
        SignedObject signedToken = validTokens.remove(0);
        usedTokens.add(signedToken);
        Capsule capsule = new Capsule(
                beginTimeInterval,
                endTimeInterval,
                signedToken,
                pseudonymHash.getBytes()
        );
        capsules.add(capsule);
        System.out.println(capsule);
        logs.add(new LogEntry((Token) signedToken.getObject(), Double.parseDouble(randomDouble), cateringFacilityInfoString, pseudonymHash.getBytes()));
        signedHash = mixingProxyImpl.registerVisit(capsule);
        System.out.println(Base64.getEncoder().encodeToString(signedHash));
    }

    @FXML
    public void leaveCateringFacility(){
        qrDataStringField.setVisible(true);
        SubmitButtonDataString.setVisible(true);
        medicButton.setVisible(true);
        infectedButton.setVisible(true);
        leaveCFButton.setVisible(false);
        FigureDisplay.setVisible(false);
        LocalDateTime leaveTime = LocalDateTime.now();
//      String[] temp =visitedCFs.get(visitedCFs.size()-1);
//      temp[1] = leaveTime.toString();

        LogEntry log = logs.get(logs.size()-1);
        log.setLeaveTime(LocalDateTime.now());
        qrDataStringField.clear();
        leavingCateringFacility = true;
    }

    @FXML
    public void submitLogsDoc() throws Exception {
        doctor.sendInfectedData(logs);
    }

    @FXML
    public void fetchInfectedCapsules() throws Exception {

        List<Capsule> infectedCapsules= matchingServiceImpl.getInfectedCapsules();  //matchingServiceImpl.getInfectedCapsules();
        List<SignedObject> infectedSignedUsertokens= new ArrayList<>();

        for(Capsule infectedCap : infectedCapsules){
            for(Capsule cap : capsules){
                if(infectedCap.compareTo(cap)){
                    infectedSignedUsertokens.add(cap.getSignedUserToken());
                    infectionAlertPane.setVisible(true);
                }
            }
        }
        //TODO return list to matching server
        matchingServiceImpl.receiveInfectedSignedUsertokens(infectedSignedUsertokens);
    }
    public void fetchNewTokens() throws Exception {
        usedTokens.addAll(validTokens);
        validTokens = registrarImpl.generateNewTokens(phoneNumber);
    }

    @FXML
    public void initialize(){
        qrDataStringField.setVisible(true);
        SubmitButtonDataString.setVisible(true);
        leaveCFButton.setVisible(false);
        FigureDisplay.setVisible(false);
        medicButton.setVisible(false);
        infectedButton.setVisible(false);
        infectionAlertPane.setVisible(false);
        try {
            Registry registrarRegistry = LocateRegistry.getRegistry("localhost",4000);
            registrarImpl = (RegistrarInterface) registrarRegistry.lookup("RegistrarService");

            Registry mixingProxyRegistery = LocateRegistry.getRegistry("localhost",4002);
            mixingProxyImpl = (MixingProxyInterface) mixingProxyRegistery.lookup("MixingProxyService");

            Registry matchingServiceRegistry = LocateRegistry.getRegistry("localhost", 4001);
            matchingServiceImpl = (MatchingServiceInterface) matchingServiceRegistry.lookup("MatchingService");
        }catch (Exception e){
            e.printStackTrace();
        }
        LocalDateTime todayMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).plusDays(1);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("New Tokens");
                    fetchNewTokens();
                    fetchInfectedCapsules();
                    System.out.println("validTokens = " );
                    for(SignedObject signedObject : validTokens){
                        System.out.println(signedObject.getObject());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        // day = 86400000
        timer.scheduleAtFixedRate(task, Duration.between(LocalDateTime.now(), todayMidnight).toMillis() , Duration.ofDays(1).toMillis());
    }

}
