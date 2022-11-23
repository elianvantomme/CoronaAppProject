package clients.visitor;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import services.mixing_proxy.MixingProxyInterface;
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
    Map<LocalDateTime, String[]> visitedCFs = new HashMap<>();
    @FXML
    private TextField qrDataStringField;
    private MixingProxyInterface mixingProxyImpl;

    private List<SignedObject> validTokens = new ArrayList<>();
    private List<SignedObject> usedTokens = new ArrayList<>();

    public void setValidTokens(List<SignedObject> validTokens) {
        this.validTokens = validTokens;
    }
    @FXML
    private void onClickSubmitDataString() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();
        String[] qrDataString = qrDataStringField.getText().split("@");
        visitedCFs.put(dateTime, qrDataString);

        String randomDouble = qrDataString[0];
        String cateringFacilityInfoString = qrDataString[1];
        String pseudonymHash = qrDataString[2];

        LocalDateTime beginTimeInterval = dateTime
                .truncatedTo(ChronoUnit.HOURS)
                .plusMinutes(30 * (dateTime.getMinute() / 30));
        LocalDateTime endTimeInterval = beginTimeInterval.plusMinutes(30);
        if (!validTokens.isEmpty()){
//            String token = validTokens.iterator().next();
//            validTokens.remove(token);
//            usedTokens.add(token);
//            Capsule capsule = new Capsule(
//                    beginTimeInterval,
//                    endTimeInterval,
//                    token,
//                    pseudonymHash
//            );
//            System.out.println(mixingProxyImpl.registerVisit(capsule));
            SignedObject signedToken = validTokens.remove(0);
            Capsule capsule = new Capsule(
                    beginTimeInterval,
                    endTimeInterval,
                    signedToken,
                    pseudonymHash
            );
            System.out.println(mixingProxyImpl.registerVisit(capsule));
        }
//        System.out.println(dateTime);
//        System.out.println(beginTimeInterval);
//        System.out.println(endTimeInterval);

    }
    @FXML
    public void initialize(){
        try {
            Registry mixingProxyRegistery = LocateRegistry.getRegistry("localhost",4002);
            mixingProxyImpl = (MixingProxyInterface) mixingProxyRegistery.lookup("MixingProxyService");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
