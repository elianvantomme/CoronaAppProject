package clients.barowner;

import java.io.Serializable;

public class CateringFacility implements Serializable {
    //all strings because numbers could contain special characters such as - or /
    private String businessNumber;
    private String name;

    public CateringFacility(String businessNumber, String name) {
        this.businessNumber = businessNumber;
        this.name = name;
    }
}
