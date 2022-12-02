package clients.barowner;

import java.io.Serializable;

public class CateringFacility implements Serializable {
    //all strings because numbers could contain special characters such as - or /
    private String businessNumber;
    private String name;
    private String address;
    private String phoneNumber;

    public CateringFacility(String businessNumber, String name, String address, String phoneNumber) {
        this.businessNumber = businessNumber;
        this.name = name;
        this.address = address;
        this.phoneNumber= phoneNumber;
    }

    @Override
    public String toString() {
        return "CateringFacility{" +
                "businessNumber='" + businessNumber + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public String printCateringFacility() {
        return name+'\n'+
                "adress='" + address + '\'' +'\n'+
                "businessNumber='" + businessNumber + '\'' +'\n' +
                "phoneNumber='" + phoneNumber + '\'';
    }
}