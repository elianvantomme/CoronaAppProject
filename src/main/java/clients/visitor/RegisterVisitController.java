package clients.visitor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
    byte[] signedHash;

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
            SignedObject signedToken = validTokens.remove(0);
            Capsule capsule = new Capsule(
                    beginTimeInterval,
                    endTimeInterval,
                    signedToken,
                    pseudonymHash
            );
            signedHash = mixingProxyImpl.registerVisit(capsule);
            System.out.println(Base64.getEncoder().encodeToString(signedHash));
            if(signedHash != null){
                Stage stage = (Stage) qrDataStringField.getScene().getWindow();
                stage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("figureControl-view.fxml"));
                Parent root = loader.load();
                FigureControlController figureControlController = loader.getController();
                figureControlController.setFigureHash(Base64.getEncoder().encodeToString(signedHash));
                stage = new Stage();
                stage.setTitle("Corona Tracing App");
                stage.setScene(new Scene(root));
                stage.show();
                //TODO: genereer een figuurtje
            } else {
                //TODO: genereer misschien een scherm waarop staat dat je een foute code hebt gestuurd
            }
        }

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
