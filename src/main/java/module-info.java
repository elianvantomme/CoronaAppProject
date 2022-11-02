module com.example.code {
    requires javafx.controls;
    requires javafx.fxml;


    opens visitor to javafx.fxml;
    exports visitor;
}