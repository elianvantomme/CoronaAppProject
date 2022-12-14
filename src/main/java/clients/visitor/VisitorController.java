package clients.visitor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.registrar.RegistrarInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.SignedObject;
import java.util.ArrayList;
import java.util.List;

public class VisitorController {
    @FXML
    private Button SubmitVisitorPhoneNumberButton;
    @FXML
    private TextField visitorPhoneNumberField;

    private RegistrarInterface registrarImpl;
    private List<SignedObject> validTokens = new ArrayList<>();

    @FXML
    public void onClickSubmitPhoneNumber() throws Exception {
        String phoneNumber = null;
        if (!visitorPhoneNumberField.getText().equals("")){
            phoneNumber = visitorPhoneNumberField.getText();
            validTokens = registrarImpl.loginVisitor(phoneNumber);
            System.out.println("validTokens = ");
            for(SignedObject signedObject : validTokens){
                System.out.println(signedObject.getObject());
            }
        }
        if (!validTokens.isEmpty()){
            Stage stage = (Stage) SubmitVisitorPhoneNumberButton.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registerVisit-view.fxml"));
            Parent root = loader.load();
            RegisterVisitController registerVisitController = loader.getController();
            registerVisitController.setValidTokens(validTokens);
            registerVisitController.setPhoneNumber(phoneNumber);
            stage = new Stage();
            stage.setTitle("Corona Tracing App");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    @FXML
    public void initialize(){
        try{
            Registry registrarRegistry = LocateRegistry.getRegistry("localhost",4000);
            registrarImpl = (RegistrarInterface) registrarRegistry.lookup("RegistrarService");

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        System.out.println("Client is ready");
    }
}
