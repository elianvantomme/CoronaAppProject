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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import services.matching_service.MatchingServiceInterface;
import services.matching_service.MatchingServiceInterfaceImp;
import services.mixing_proxy.MixingProxyInterface;
import services.registrar.RegistrarContent;
import services.registrar.RegistrarInterface;
import services.registrar.RegistrarInterfaceImpl;
import services.registrar.Token;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.SignedObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class RegisterVisitController {
    //TODO gaan verwijderen van de visistedCFs na een bepaald aantal dagen
//    Map<LocalDateTime, String[]> visitedCFs = new HashMap<>();
    private ArrayList<String[]> visitedCFs;
    private ArrayList<Capsule> capsules;
    private DoctorClient doctor;
    private MixingProxyInterface mixingProxyImpl;
    private MatchingServiceInterface matchingServiceImpl;
    private List<SignedObject> validTokens;
    private List<SignedObject> usedTokens;
    private byte[] signedHash;
    private List<LogEntry> logs;

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

    public RegisterVisitController() throws Exception {
        this.visitedCFs = new ArrayList<>();
        this.capsules = new ArrayList<>();
        this.doctor = new DoctorClient();
        this.validTokens = new ArrayList<>();
        this.usedTokens = new ArrayList<>();
        this.logs = new ArrayList<>();
    }

    public void setValidTokens(List<SignedObject> validTokens) {
        this.validTokens = validTokens;
    }


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

        LocalDateTime beginTimeInterval = dateTime
                .truncatedTo(ChronoUnit.HOURS)
                .plusMinutes(30 * (dateTime.getMinute() / 30));
        LocalDateTime endTimeInterval = beginTimeInterval.plusMinutes(30);
        if (!validTokens.isEmpty()){
            SignedObject signedToken = validTokens.remove(0);
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
            if(signedHash != null){
                qrDataStringField.setVisible(false);
                SubmitButtonDataString.setVisible(false);
                medicButton.setVisible(false);
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
            } else {
                //TODO: genereer misschien een scherm waarop staat dat je een foute code hebt gestuurd
            }
        }
    }
    @FXML
    public void leaveCateringFacility(){
        qrDataStringField.setVisible(true);
        SubmitButtonDataString.setVisible(true);
        medicButton.setVisible(true);
        leaveCFButton.setVisible(false);
        FigureDisplay.setVisible(false);
        LocalDateTime leaveTime = LocalDateTime.now();
//      String[] temp =visitedCFs.get(visitedCFs.size()-1);
//      temp[1] = leaveTime.toString();

        LogEntry log = logs.get(logs.size()-1);
        log.setLeaveTime(LocalDateTime.now());
        qrDataStringField.clear();
    }

    @FXML
    public void submitLogsDoc(){

    }

    @FXML
    public void fetchInfectedCapsules() throws Exception{
        List<Capsule> infectedCapsuleList = matchingServiceImpl.getInfectedCapsules();
    }
    @FXML
    public void initialize(){
        qrDataStringField.setVisible(true);
        SubmitButtonDataString.setVisible(true);
        leaveCFButton.setVisible(false);
        FigureDisplay.setVisible(false);
        medicButton.setVisible(false);
        try {
            Registry mixingProxyRegistery = LocateRegistry.getRegistry("localhost",4002);
            mixingProxyImpl = (MixingProxyInterface) mixingProxyRegistery.lookup("MixingProxyService");

            Registry matchingServiceRegistry = LocateRegistry.getRegistry("localhost", 4001);
            matchingServiceImpl = (MatchingServiceInterface) matchingServiceRegistry.lookup("MatchingService");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
