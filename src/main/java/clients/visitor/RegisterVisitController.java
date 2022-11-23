package clients.visitor;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RegisterVisitController {

    Map<LocalDateTime, String[]> visitedCFs = new HashMap<>();
    @FXML
    private TextField qrDataStringField;

    private Set<String> validTokens = new HashSet<>();

    public void setValidTokens(Set<String> validTokens) {
        this.validTokens = validTokens;
    }
    @FXML
    private void onClickSubmitDataString(){
        LocalDateTime dateTime = LocalDateTime.now();
        String[] qrDataString = qrDataStringField.getText().split("@");
        visitedCFs.put(dateTime, qrDataString);

        String randomDouble = qrDataString[0];
        String cateringFacilityInfoString = qrDataString[1];
        String pseudonymHash = qrDataString[2];

        System.out.println(randomDouble);
        System.out.println(cateringFacilityInfoString);
        System.out.println(pseudonymHash);

    }

}
