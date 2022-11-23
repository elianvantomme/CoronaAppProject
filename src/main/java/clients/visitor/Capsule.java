package clients.visitor;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Capsule implements Serializable {
    private LocalDateTime startInterval;
    private LocalDateTime endInterval;
    private String userToken;
    private String hashRandomNym;

    public Capsule(LocalDateTime startInterval, LocalDateTime endInterval, String userToken, String hashRandomNym) {
        this.startInterval = startInterval;
        this.endInterval = endInterval;
        this.userToken = userToken;
        this.hashRandomNym = hashRandomNym;
    }

    public LocalDateTime getStartInterval() {
        return startInterval;
    }

    public LocalDateTime getEndInterval() {
        return endInterval;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getHashRandomNym() {
        return hashRandomNym;
    }
}
