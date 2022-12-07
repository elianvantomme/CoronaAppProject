package services.matching_service;

import clients.visitor.Capsule;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class MatchingServiceContent {
    static private List<Capsule> capsuleList;
    static private List<Capsule> uninformdInfectedCapsules;
    static private List<Capsule> informdInfectedCapsules;
    static private List<PublicKey> doctorPublicKeys;

    public MatchingServiceContent(){
        capsuleList = new ArrayList<>();
        uninformdInfectedCapsules = new ArrayList<>();
        informdInfectedCapsules = new ArrayList<>();
        doctorPublicKeys = new ArrayList<>();
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

    public String printCapsuleList(){
        StringBuilder sb = new StringBuilder();
        for (Capsule capsule : capsuleList) {
            sb.append(capsule);
        }
        return sb.toString();
    }

    public String printUninformedCapsuleList(){
        StringBuilder sb = new StringBuilder();
        for (Capsule capsule : uninformdInfectedCapsules) {
            sb.append(capsule);
        }
        return sb.toString();
    }

    public String printInformedCapsuleList(){
        StringBuilder sb = new StringBuilder();
        for (Capsule capsule : informdInfectedCapsules) {
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
}
