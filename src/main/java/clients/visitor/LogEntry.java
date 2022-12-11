package clients.visitor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

public class LogEntry implements Serializable {

    private double random;
    private String cf;
    private byte[] hash;
    private LocalDateTime entryTime;
    private LocalDateTime leaveTime;

    LogEntry(double random, String cf, byte[] hash){
        this.random = random;
        this.cf = cf;
        this.hash =hash;
        this.entryTime = LocalDateTime.now();
    }

    public LogEntry(double random, String cf, byte[] hash, LocalDateTime entryTime, LocalDateTime leaveTime) {
        this.random = random;
        this.cf = cf;
        this.hash = hash;
        this.entryTime = entryTime;
        this.leaveTime = leaveTime;
    }

    public void setLeaveTime(LocalDateTime leaveTime){
        this.leaveTime= leaveTime;
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
        return random +
                "," + cf + '\'' +
                "," + Arrays.toString(hash) +
                "," + entryTime +
                "," + leaveTime;
    }

    public String printForLog() {
        return random +
                "/" + cf + '\'' +
                "/" + Arrays.toString(hash) +
                "/" + entryTime +
                "/" + leaveTime;
    }
}
