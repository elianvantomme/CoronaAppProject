package clients.visitor;

import services.registrar.Token;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

public class LogEntry implements Serializable {

    private Token token;
    private double random;
    private String cf;
    private byte[] hash;
    private LocalDateTime entryTime;
    private LocalDateTime leaveTime;

    LogEntry(Token token, double random, String cf, byte[] hash){
        this.token = token;
        this.random = random;
        this.cf = cf;
        this.hash = hash;
        this.entryTime = LocalDateTime.now();
    }
    public void setLeaveTime(LocalDateTime leaveTime){
        this.leaveTime= leaveTime;
    }

    public Token getToken() {
        return token;
    }

    public double getRandom() {
        return random;
    }

    public String getCf() {
        return cf;
    }

    public byte[] getHash() {
        return hash;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getLeaveTime() {
        return leaveTime;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                token +
                "," + random +
                "," + cf + '\'' +
                "," + Arrays.toString(hash) +
                "," + entryTime +
                "," + leaveTime +
                '}';
    }
}
