package services.registrar;

import clients.barowner.CateringFacility;

import java.util.ArrayList;
import java.util.List;

public class RegistrarContent {
    static private List<String> visitorList;
    static private List<CateringFacility> cateringFacilityList;

    public RegistrarContent() {
        visitorList = new ArrayList<>();
        cateringFacilityList = new ArrayList<>();
    }

    public void addCateringFacility(CateringFacility cateringFacility) {
        cateringFacilityList.add(cateringFacility);
    }

    public void addVisitor(String visitor){
        visitorList.add(visitor);
    }

    public String printVisitorList() {
        StringBuilder sb = new StringBuilder();
        for (String visitor : visitorList) {
            sb.append(visitor).append("\n");
        }
        return sb.toString();
    }

    public String printCateringFacilityList() {
        StringBuilder sb = new StringBuilder();
        for (CateringFacility cf:cateringFacilityList) {
            sb.append(cf.printCateringFacility()).append("\n").append("\n");
        }
        return sb.toString();
    }
}
