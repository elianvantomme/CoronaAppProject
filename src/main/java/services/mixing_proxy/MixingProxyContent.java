package services.mixing_proxy;

import clients.visitor.Capsule;

import java.util.ArrayList;
import java.util.List;

public class MixingProxyContent {
    static private List<Capsule> capsuleList;

    public MixingProxyContent(){
        capsuleList = new ArrayList<>();
    }

    public void addCapsule(Capsule capsule){
        capsuleList.add(capsule);
    }
    public List<Capsule> getCapsuleList() {
        return capsuleList;
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
