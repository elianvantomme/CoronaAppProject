package services.registrar;

import clients.barowner.CateringFacility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrarContent {
    private static List<String> visitorList;
    private static List<CateringFacility> cateringFacilityList;
    private static Map<String, List<Token>> oldTokensMap;
    private static Map<String, List<Token>> validTokensMap;

    public RegistrarContent() {
        visitorList = new ArrayList<>();
        cateringFacilityList = new ArrayList<>();
        oldTokensMap = new HashMap<>();
        validTokensMap = new HashMap<>();
    }

    public void addCateringFacility(CateringFacility cateringFacility) {
        cateringFacilityList.add(cateringFacility);
    }

    public void addVisitor(String visitor){
        visitorList.add(visitor);
    }

    public void addOldTokens(String key, List<Token> tokens){
        oldTokensMap.put(key, tokens);
    }

    public Map<String, List<Token>> getOldTokensMap() {
        return oldTokensMap;
    }

    public List<Token> getValidTokens(String phoneNumber){
        return validTokensMap.get(phoneNumber);
    }

    public static List<CateringFacility> getCateringFacilityList() {
        return cateringFacilityList;
    }

    public void addValidTokens(String key, List<Token> tokens){
        validTokensMap.put(key,tokens);
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

    public boolean containsCF(CateringFacility cateringFacility) {
        for (CateringFacility cf : cateringFacilityList) {
            if (cf.equals(cateringFacility)){
                return true;
            }
        }
        return false;
    }
}
