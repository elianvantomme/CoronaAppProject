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

    public boolean compareTo(Capsule capsule) throws Exception{
        if(!this.startInterval.equals(capsule.getStartInterval())) return false;
        if(!this.endInterval.equals(capsule.getEndInterval())) return false;

        Token token1 = (Token) this.signedUserToken.getObject();
        Token token2 = (Token) capsule.getSignedUserToken().getObject();

        if(!token1.equals(token2)) return false;
        if(!Arrays.equals(this.hashRandomNym, capsule.getHashRandomNym())) return false;

        return true;
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
