module visitor.clients {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;

    exports clients.visitor;
    exports services.registrar;

    opens clients.visitor;
}
