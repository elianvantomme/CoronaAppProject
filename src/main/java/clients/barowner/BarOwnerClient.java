package clients.barowner;

import services.registrar.RegistrarInterface;

import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class BarOwnerClient {
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

            generateDailyQRCode(registrarImpl, cateringFacility);
            Timer timer = new Timer();
            LocalDateTime todayMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).plusDays(1);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        generateDailyQRCode(registrarImpl, cateringFacility);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
            timer.scheduleAtFixedRate(task, Duration.between(LocalDateTime.now(), todayMidnight).toMillis() , Duration.ofDays(1).toMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateDailyQRCode(RegistrarInterface registrarImpl, CateringFacility cateringFacility) throws Exception {
        pseudonym = registrarImpl.loginCF(cateringFacility);
        System.out.println(pseudonym);
        System.out.println("The qr data string for");
        double randomDouble = Math.random();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] byteArray = String.valueOf(randomDouble).concat(pseudonym).getBytes(StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        sb.append(randomDouble).append("@")
                .append(cateringFacility).append("@")
                .append(Base64.getEncoder().encodeToString(md.digest(byteArray)));
        String qrDataString = sb.toString();
        System.out.println(qrDataString);
    }

    public static void main(String[] args) {
        BarOwnerClient main = new BarOwnerClient();
        main.runClient();
    }
}
