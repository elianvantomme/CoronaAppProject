package clients.barowner;

import services.registrar.RegistrarInterface;

import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class BarOwnerClient {
    private Set<String> pseudonymSet = new HashSet<>();
    private Scanner sc = new Scanner(System.in);
    private String pseudonym;

    public void runClient(){
        try {
            Registry registrarRegistry = LocateRegistry.getRegistry("localhost", 4000);
            RegistrarInterface registrarImpl = (RegistrarInterface) registrarRegistry.lookup("RegistrarService");

            System.out.println("Enter the name of the facility");
            String name = sc.nextLine();
            System.out.println("Enter the business number of the facility");
            String businessNumber = sc.nextLine();
            System.out.println("Enter the location of the facility");
            String address = sc.nextLine();
            System.out.println("Enter the phone number of the facility");
            String phoneNumber = sc.nextLine();
            CateringFacility cateringFacility = new CateringFacility(businessNumber, name, address, phoneNumber);

            pseudonym = registrarImpl.loginCF(cateringFacility);
            System.out.println(pseudonym);
            System.out.println("The qr data string for");
            //TODO generate the QR code based on the random value, CF info, hash
            double randomDouble = Math.random()*10000;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] byteArray = String.valueOf(randomDouble).concat(pseudonym).getBytes(StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            sb.append(randomDouble).append("@")
                    .append(cateringFacility.toString()).append("@")
                    .append(Base64.getEncoder().encodeToString(md.digest(byteArray)));
            String qrDataString = sb.toString();
            System.out.println(qrDataString);
            while(true){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        BarOwnerClient main = new BarOwnerClient();
        main.runClient();
    }
}
