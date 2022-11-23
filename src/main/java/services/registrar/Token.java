package services.registrar;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;

public class Token implements Serializable {
    private double random;
    private LocalDate localDate;

    Token(){
        random = Math.random();
        localDate = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Token{" +
                "random=" + random +
                ", localDate=" + localDate +
                '}';
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

}
