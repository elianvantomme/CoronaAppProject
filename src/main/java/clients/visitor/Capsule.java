package clients.visitor;

import java.io.Serializable;
import java.security.SignedObject;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Capsule implements Serializable {
    private LocalDateTime startInterval;
    private LocalDateTime endInterval;
    private SignedObject signedUserToken;
    private byte[] hashRandomNym;

    public Capsule(LocalDateTime startInterval, LocalDateTime endInterval, SignedObject userToken, byte[] hashRandomNym) {
        this.startInterval = startInterval;
        this.endInterval = endInterval;
        this.signedUserToken = userToken;
        this.hashRandomNym = hashRandomNym;
    }

    public LocalDateTime getStartInterval() {
        return startInterval;
    }

    public LocalDateTime getEndInterval() {
        return endInterval;
    }

    public SignedObject getSignedUserToken() {
        return signedUserToken;
    }

    public byte[] getHashRandomNym() {
        return hashRandomNym;
    }

    @Override
    public String toString() {
        return "Capsule:"+ "\n"+
                "startInterval=" + startInterval + "\n"+
                ", endInterval=" + endInterval + "\n"+
                ", signedUserToken=" + signedUserToken + "\n"+
                ", hashRandomNym=" + Arrays.toString(hashRandomNym) + "\n";
    }
}
