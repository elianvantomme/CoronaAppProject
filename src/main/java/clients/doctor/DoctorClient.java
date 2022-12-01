package clients.doctor;

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

    public void sendInfectedData(ArrayList<LogEntry> logs) throws Exception{
        SignedObject signedLogs = new SignedObject(logs, keyPair.getPrivate(), signature);
        matchingServiceImpl.receiveLogs(signedLogs);
    }

}
