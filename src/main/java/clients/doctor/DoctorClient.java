package clients.doctor;

import clients.visitor.LogEntry;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import services.matching_service.MatchingServiceInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.SignedObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DoctorClient {

    private Signature signature;
    private KeyPair keyPair;
    private MatchingServiceInterface matchingServiceImpl;

    public DoctorClient() throws Exception{
        keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(keyPair.getPrivate());
        Registry matchingServiceRegistry = LocateRegistry.getRegistry("localhost",4001);
        matchingServiceImpl = (MatchingServiceInterface) matchingServiceRegistry.lookup("MatchingService");
        matchingServiceImpl.addDoctorPublicKey(keyPair.getPublic());
    }

    @FXML
    public void uploadPatientFile() throws Exception {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
        File f = fc.showOpenDialog(null);
        ArrayList<LogEntry> logs = new ArrayList<>();
        if (f != null){
            System.out.println(f.getAbsolutePath());
            Scanner fileReader = new Scanner(f);
            while (fileReader.hasNext()){
                String[] parts = fileReader.nextLine().split("/");
                double random = Double.parseDouble(parts[0]);
                String cf = parts[1];
                String[] tempString = parts[2].split("\\[");
                String[] finalString = tempString[1].split("]");
                System.out.println(finalString[0]);
                String[] byteString = finalString[0].split(", ");
                System.out.println(byteString[0]);
                byte[] hash = new byte[byteString.length];
                for (int i = 0; i < byteString.length; i++) {
                    hash[i] = Byte.parseByte(byteString[i]);
                }
                System.out.println(Arrays.toString(hash));
                LocalDateTime entryTime = LocalDateTime.parse(parts[3]);
                LocalDateTime leaveTime = LocalDateTime.parse(parts[4]);
                LogEntry logEntry = new LogEntry(random, cf, hash, entryTime, leaveTime);
                logs.add(logEntry);
            }
            sendInfectedData(logs);
            fileReader.close();
        }
    }

    public void sendInfectedData(ArrayList<LogEntry> logs) throws Exception{
        SignedObject signedLogs = new SignedObject(logs, keyPair.getPrivate(), signature);
        matchingServiceImpl.receiveLogs(signedLogs);
    }

}
