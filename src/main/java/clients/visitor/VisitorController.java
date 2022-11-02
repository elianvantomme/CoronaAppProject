package clients.visitor;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import services.registrar.RegistrarInterface;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class VisitorController {
    @FXML
    private Button SubmitVisitorPhoneNumberButton;
    @FXML
    private TextField visitorPhoneNumberField;

    private RegistrarInterface registrarImpl;
    private Set<String> validTokens = new HashSet<>();

    @FXML
    public void onClickSubmitPhoneNumber() throws Exception {
        if (!visitorPhoneNumberField.getText().equals("")){
            validTokens = registrarImpl.loginVisitor(visitorPhoneNumberField.getText());
            System.out.println(Arrays.toString(validTokens.toArray()));
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
