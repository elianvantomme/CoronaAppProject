package services.registrar;

import clients.barowner.CateringFacility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class RegistrarContent {
    static private List<String> visitorList;
    static private List<CateringFacility> cateringFacilityList;
    static private Map<String, List<Token>> oldTokensMap;
    static private Map<String, List<Token>> validTokensMap;

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
}
