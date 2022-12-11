package services.mixing_proxy;

import clients.visitor.Capsule;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class MixingProxyContent {
    private static List<Capsule> capsuleList;
    private static PublicKey registrarPublicKey;

    public MixingProxyContent(){
        capsuleList = new ArrayList<>();
    }

    public void addCapsule(Capsule capsule){
        capsuleList.add(capsule);
    }
    public List<Capsule> getCapsuleList() {
        return capsuleList;
    }

    public void setRegistrarPublicKey(PublicKey registrarPublicKey) {
        MixingProxyContent.registrarPublicKey = registrarPublicKey;
    }

    public PublicKey getRegistrarPublicKey() {
        return registrarPublicKey;
    }

    public String printContent() {
        StringBuilder sb = new StringBuilder();
        int counter = 1;
        for (Capsule capsule: capsuleList) {
            sb.append(counter).append(") ").append(capsule.toString()).append("\n").append("\n");
            counter++;
        }
        return sb.toString();
    }
}
