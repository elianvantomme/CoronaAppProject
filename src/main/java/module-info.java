module com.example.code {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;


    opens visitor to javafx.fxml;
    exports visitor;
}