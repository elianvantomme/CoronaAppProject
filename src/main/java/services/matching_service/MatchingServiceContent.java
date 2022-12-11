package services.matching_service;

import clients.visitor.Capsule;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MatchingServiceContent {
    private static List<Capsule> capsuleList;
    private static List<Capsule> uninformdInfectedCapsules;
    private static List<Capsule> informdInfectedCapsules;
    private static List<PublicKey> doctorPublicKeys;
    private static List<String> cateringFacilityNym;

    public MatchingServiceContent(){
        capsuleList = new ArrayList<>();
        uninformdInfectedCapsules = new ArrayList<>();
        informdInfectedCapsules = new ArrayList<>();
        doctorPublicKeys = new ArrayList<>();
        cateringFacilityNym = new ArrayList<>();
    }

    public void addDoctorKey(PublicKey key){
        doctorPublicKeys.add(key);
    }

    public List<Capsule> getCapsuleList() {
        return capsuleList;
    }

    public List<Capsule> getUninformdInfectedCapsules() {
        return uninformdInfectedCapsules;
    }

    public List<Capsule> getInformdInfectedCapsules() {
        return informdInfectedCapsules;
    }

    public List<PublicKey> getDoctorPublicKeys() {
        return doctorPublicKeys;
    }

    public void addCapsulesToList(List<Capsule> capsuleList) {
        MatchingServiceContent.capsuleList.addAll(capsuleList);
    }

    public void addUniformedCapsule(Capsule capsule){
        uninformdInfectedCapsules.add(capsule);
    }

    public void removeNormalCapsule(Capsule capsule){
        capsuleList.remove(capsule);
    }

    public void addNewPseudonyms(List<String> pseudonyms) {
        cateringFacilityNym.clear();
        cateringFacilityNym.addAll(pseudonyms);
    }

    public String printCapsuleList(){
        StringBuilder sb = new StringBuilder();
        Iterator<Capsule> iterator = capsuleList.iterator();
        while(iterator.hasNext()){
            Capsule capsule = iterator.next();
            sb.append(capsule);
        }
        return sb.toString();
    }

    public String printUninformedCapsuleList(){
        StringBuilder sb = new StringBuilder();
        Iterator<Capsule> iterator = uninformdInfectedCapsules.iterator();
        while(iterator.hasNext()){
            Capsule capsule = iterator.next();
            sb.append(capsule);
        }
        return sb.toString();
    }

    public String printInformedCapsuleList(){
        StringBuilder sb = new StringBuilder();
        Iterator<Capsule> iterator = informdInfectedCapsules.iterator();
        while(iterator.hasNext()){
            Capsule capsule = iterator.next();
            sb.append(capsule);
        }
        return sb.toString();
    }

    public void addInformedCapsule(Capsule uninformedCapsule) {
        informdInfectedCapsules.add(uninformedCapsule);
    }

    public void removeUniformedCapsule(Capsule uninformedCapsule) {
        uninformdInfectedCapsules.remove(uninformedCapsule);
    }

    public void setCapsuleList(List<Capsule> tempList) {
        capsuleList = tempList;
    }

    public String printPseudonyms() {
        StringBuilder sb = new StringBuilder();
        for (String string : cateringFacilityNym) {
            sb.append(string).append("\n");
        }
        return sb.toString();
    }
}
