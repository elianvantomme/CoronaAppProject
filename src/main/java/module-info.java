module visitor.clients {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;

    exports clients.visitor;
    exports services.registrar;
    exports services.mixing_proxy;
    exports services.matching_service;

    opens clients.visitor;
}
