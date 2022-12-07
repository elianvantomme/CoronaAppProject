package clients.visitor;

import services.registrar.Token;

import java.io.IOException;
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
        Token token = null;
        try {
            token = (Token) signedUserToken.getObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
            return "Capsule:"+ "\n"+
                "startInterval=" + startInterval + "\n"+
                ", endInterval=" + endInterval + "\n"+
                ", signedUserToken=" + token.toString()+ "\n"+
                ", hashRandomNym=" + Arrays.toString(hashRandomNym) + "\n";
    }
}
