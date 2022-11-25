package clients.visitor;

import java.io.Serializable;
import java.security.SignedObject;
import java.time.LocalDateTime;

public class Capsule implements Serializable {
    private LocalDateTime startInterval;
    private LocalDateTime endInterval;
    private SignedObject signedUserToken;
    private String hashRandomNym;

    public Capsule(LocalDateTime startInterval, LocalDateTime endInterval, SignedObject userToken, String hashRandomNym) {
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

    public String getHashRandomNym() {
        return hashRandomNym;
    }
}