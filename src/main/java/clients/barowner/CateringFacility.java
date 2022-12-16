package clients.barowner;

import java.io.Serializable;
import java.util.Objects;

public class CateringFacility implements Serializable {
    //all strings because numbers could contain special characters such as - or /
    private String businessNumber;
    private String name;
    private String address;
    private String phoneNumber;
    private String pseudonym;

    public CateringFacility(String businessNumber, String name, String address, String phoneNumber) {
        this.businessNumber = businessNumber;
        this.name = name;
        this.address = address;
        this.phoneNumber= phoneNumber;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public String getAddress() {
        return address;
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
                "phoneNumber='" + phoneNumber + '\''+'\n' +
                "pseudonym='" + pseudonym + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CateringFacility that = (CateringFacility) o;
        return Objects.equals(businessNumber, that.businessNumber) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessNumber, name, address, phoneNumber);
    }
}