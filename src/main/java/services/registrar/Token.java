package services.registrar;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Double.compare(token.random, random) == 0 && Objects.equals(localDate, token.localDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(random, localDate);
    }
}
