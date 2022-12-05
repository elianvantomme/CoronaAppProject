module visitor.clients {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;

    exports clients.visitor;
    exports services.registrar;
    exports services.mixing_proxy;
    exports services.matching_service;
    exports clients.barowner;

    opens clients.visitor;
    opens services.registrar;
    opens services.mixing_proxy;
    opens services.matching_service;
}
