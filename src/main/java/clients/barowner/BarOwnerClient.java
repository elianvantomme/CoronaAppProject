package clients.barowner;

import services.registrar.RegistrarInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class BarOwnerClient {

    private Scanner sc = new Scanner(System.in);
    private String pseudonym;
    private CateringFacility cateringFacility = new CateringFacility("123", "tibo's bar");

    public void runClient() {
        try {
            Registry registrarRegistry = LocateRegistry.getRegistry("localhost", 4000);
            RegistrarInterface registrarImpl = (RegistrarInterface) registrarRegistry.lookup("RegistrarService");

            System.out.print("Enter the phone number of the facility: ");
//            int cfPhoneNumber = Integer.parseInt(sc.nextLine());
//            pseudonym = registrarImpl.loginCF(cfPhoneNumber);
            pseudonym = registrarImpl.enrollCF(cateringFacility);
//            System.out.println(pseudonym);

            //TODO generate the QR code based on the random value, CF info, hash
            while (true) {

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
